package org.comroid.crystalshard.ui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.comroid.api.ContextualProvider;
import org.comroid.api.Named;
import org.comroid.crystalshard.Context;
import org.comroid.crystalshard.DiscordBotBase;
import org.comroid.crystalshard.entity.command.Command;
import org.comroid.crystalshard.entity.message.Message;
import org.comroid.crystalshard.gateway.event.dispatch.interaction.InteractionCreateEvent;
import org.comroid.crystalshard.model.command.CommandInteractionData;
import org.comroid.crystalshard.model.command.CommandInteractionDataOption;
import org.comroid.crystalshard.model.command.CommandOption;
import org.comroid.crystalshard.model.message.embed.Embed;
import org.comroid.crystalshard.rest.Endpoint;
import org.comroid.mutatio.span.Span;
import org.comroid.restless.REST;
import org.comroid.uniform.node.UniArrayNode;
import org.comroid.uniform.node.UniNode;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.util.StreamOPs;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InteractionCore implements Context {
    private static final Logger logger = LogManager.getLogger();
    private final DiscordBotBase bot;
    private final CommandSetup config;

    @Override
    public ContextualProvider getUnderlyingContextualProvider() {
        return bot;
    }

    public CommandSetup getCommands() {
        return config;
    }

    public InteractionCore(DiscordBotBase bot) {
        this.bot = bot;
        this.config = new CommandSetup(this);

        bot.getEventPipeline()
                .flatMap(InteractionCreateEvent.class)
                .forEach(this::handleInteraction);
    }

    public CompletableFuture<List<Command>> requestGlobalCommands() {
        return getBot().newRequest(
                REST.Method.GET,
                Endpoint.APPLICATION_COMMANDS_GLOBAL.complete(getBot().getOwnID()),
                UniNode::asArrayNode
        ).<List<Command>>thenApply(array -> array == null ? new ArrayList<>() : array.streamNodes()
                .map(data -> Command.resolve(this, data))
                .collect(Collectors.toList()))
                .thenApply(Collections::unmodifiableList);
    }

    public CompletableFuture<List<Command>> requestGuildCommands(long guildId) {
        return getBot().newRequest(
                REST.Method.GET,
                Endpoint.APPLICATION_COMMANDS_GUILD.complete(getBot().getOwnID(), guildId),
                UniNode::asArrayNode
        ).<List<Command>>thenApply(array -> array == null ? new ArrayList<>() : array.streamNodes()
                .map(data -> Command.resolve(this, data))
                .collect(Collectors.toList()))
                .thenApply(Collections::unmodifiableList);
    }

    public CompletableFuture<Void> synchronizeGlobal() {
        final Set<CommandDefinition> globalDefinitions = config.getGlobalDefinitions();
        logger.debug("Synchronizing {} globally defined Commands...", globalDefinitions.size());

        //noinspection unchecked
        return requestGlobalCommands()
                .thenApply(registered -> {
                    final List<String> existingNames = StreamOPs.map(registered, Command::getName);

                    return Stream.concat(
                            // update existing
                            registered.stream()
                                    .filter(cmd -> config.hasCommand(cmd.getName()))
                                    .map(cmd -> {
                                        CommandDefinition def = config.getCommand(cmd.getName());
                                        if (def == null)
                                            return null;
                                        if (isSyncedCorrectly(cmd, def))
                                            return CompletableFuture.completedFuture(cmd);
                                        /*
                                        TODO:
                                        endpoint seems to be broken at discords side;
                                        always returns 400 - command already exists

                                        return updateGlobalCommand(cmd, def);

                                        temporarily replaced with re-registering; as it will overwrite the old command
                                         */
                                        return registerGlobalCommand(def);
                                    })
                                    .filter(Objects::nonNull)
                            , // create nonexisting
                            globalDefinitions.stream()
                                    .filter(def -> !existingNames.contains(def.getName()))
                                    .map(this::registerGlobalCommand)
                    ).toArray(CompletableFuture<?>[]::new);
                }).thenCompose(CompletableFuture::allOf);
    }

    public CompletableFuture<Void> synchronizeGuild(long guildId) {
        final Set<CommandDefinition> globalDefinitions = config.getGuildDefinitions(guildId);
        logger.debug("Synchronizing {} defined Commands for Guild {}...", globalDefinitions.size(), guildId);

        //noinspection unchecked
        return requestGuildCommands(guildId)
                .thenApply(registered -> {
                    final List<String> existingNames = StreamOPs.map(registered, Command::getName);

                    return Stream.concat(
                            // update existing
                            registered.stream()
                                    .filter(cmd -> config.hasCommand(cmd.getName()))
                                    .map(cmd -> {
                                        CommandDefinition def = config.getCommand(cmd.getName());
                                        if (def == null)
                                            return null;
                                        if (isSyncedCorrectly(cmd, def)) {
                                            logger.info("Not syncing {} as it is synced correctly", cmd);
                                            return CompletableFuture.completedFuture(cmd);
                                        }
                                        /*
                                        TODO:
                                        endpoint seems to be broken at discords side;
                                        always returns 400 - command already exists

                                        return updateGuildCommand(guildId, cmd, def);

                                        temporarily replaced with re-registering; as it will overwrite the old command
                                         */
                                        return registerGuildCommand(guildId, def);
                                    })
                                    .filter(Objects::nonNull)
                            , // create nonexisting
                            globalDefinitions.stream()
                                    .filter(def -> !existingNames.contains(def.getName()))
                                    .map(def -> registerGuildCommand(guildId, def))
                    ).toArray(CompletableFuture<?>[]::new);
                }).thenCompose(CompletableFuture::allOf);
    }

    private boolean isSyncedCorrectly(Command cmd, CommandDefinition def) {
        return cmd.name.equals(def.name)
                && cmd.description.equals(def.description)
                && optionsEqual(cmd, def);
    }

    private boolean optionsEqual(Command command, CommandDefinition definition) {
        final Span<CommandOption> optionsDef = definition.getComputedReference(Command.OPTIONS).orElseGet(Span::empty);
        final Map<String, CommandOption> optionsSet = command.getComputedReference(Command.OPTIONS).orElseGet(Span::empty)
                .stream()
                .collect(Collectors.toMap(CommandOption::getName, Function.identity()));

        if (optionsDef.isEmpty() && optionsSet.isEmpty())
            return true;
        return optionsDef.stream()
                .allMatch(opt -> optionsSet.containsKey(opt.getName()) && optionsSet.get(opt.getName()).equals(opt));
    }

    private CompletableFuture<Command> updateGlobalCommand(Command original, CommandDefinition cmd) {
        return getBot().newRequest(
                REST.Method.PATCH,
                Endpoint.APPLICATION_COMMANDS_GLOBAL_SPECIFIC.complete(getBot().getOwnID(), original.getID()),
                cmd,
                Command.TYPE
        );
    }

    private CompletableFuture<Command> registerGlobalCommand(CommandDefinition cmd) {
        return getBot().newRequest(
                REST.Method.POST,
                Endpoint.APPLICATION_COMMANDS_GLOBAL.complete(getBot().getOwnID()),
                cmd,
                Command.TYPE
        );
    }

    private CompletableFuture<Command> updateGuildCommand(long guildId, Command original, CommandDefinition cmd) {
        return getBot().newRequest(
                REST.Method.PATCH,
                Endpoint.APPLICATION_COMMANDS_GUILD_SPECIFIC.complete(getBot().getOwnID(), guildId, original.getID()),
                cmd,
                Command.TYPE
        );
    }

    private CompletableFuture<Command> registerGuildCommand(long guildId, CommandDefinition cmd) {
        return getBot().newRequest(
                REST.Method.POST,
                Endpoint.APPLICATION_COMMANDS_GUILD.complete(getBot().getOwnID(), guildId),
                cmd,
                Command.TYPE
        );
    }

    private void handleInteraction(InteractionCreateEvent event) {
        final Interaction interaction = event.getInteraction();
        final UniObjectNode response = getSerializer().createUniObjectNode();

        if (interaction.getType() == Interaction.Type.PING)
            response.put("type", InteractionResponseType.PONG);
        else {
            final CommandInteractionData data = interaction.getData();
            final CommandDefinition definition = config.getCommand(data.getCommandName());
            if (definition == null)
                throw new AssertionError("Unrecognized command: " + data.getCommand());
            final Map<String, CommandInteractionDataOption> options = data.getOptions()
                    .stream()
                    .collect(Collectors.toMap(Named::getName, Function.identity()));
            final Method method = definition.getMethod();
            final Parameter[] parameters = method.getParameters();
            final Object[] args = new Object[parameters.length];

            for (int i = 0; i < parameters.length; i++) {
                final String name = parameters[i].getName();
                final CommandInteractionDataOption option = options.get(name);

                if (option == null) {
                    args[i] = event.getFromContext(parameters[i].getType()).orElse(null);
                    continue;
                }

                // todo Handle Subcommand case ??

                args[i] = option.getValue();
            }

            try {
                final Object yield = method.invoke(definition.getTarget(), args);

                if (yield instanceof String) {
                    response.put("type", InteractionResponseType.CHANNEL_MESSAGE);
                    final UniObjectNode responseMessage = response.putObject("data");
                    responseMessage.put(Message.CONTENT, String.valueOf(yield));
                } else if (yield instanceof Embed) {
                    response.put("type", InteractionResponseType.CHANNEL_MESSAGE);
                    final UniObjectNode responseMessage = response.putObject("data");
                    responseMessage.put(Message.CONTENT, "");
                    final UniArrayNode embeds = responseMessage.putArray(Message.EMBEDS);
                    ((Embed) yield).toObjectNode(embeds.addObject());
                } else if (yield == null) {
                    response.put("type", InteractionResponseType.ACKNOWLEDGE_WITH_SOURCE);
                } else response.put("type", InteractionResponseType.ACKNOWLEDGE);
            } catch (Throwable t) {
                response.put("type", InteractionResponseType.CHANNEL_MESSAGE_WITH_SOURCE);

                final UniObjectNode responseMessage = response.putObject("data");
                responseMessage.put(Message.CONTENT, "The Command caused an internal exception: " + t);
            }
        }

        getBot().newRequest(
                REST.Method.POST,
                Endpoint.INTERACTION_CALLBACK.complete(interaction.getId(), interaction.getContinuationToken()),
                response
        ).join();
    }
}

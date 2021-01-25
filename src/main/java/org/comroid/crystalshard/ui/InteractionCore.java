package org.comroid.crystalshard.ui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.Context;
import org.comroid.crystalshard.DiscordBotBase;
import org.comroid.crystalshard.entity.command.Command;
import org.comroid.crystalshard.gateway.event.dispatch.interaction.InteractionCreateEvent;
import org.comroid.crystalshard.rest.Endpoint;
import org.comroid.restless.REST;
import org.comroid.uniform.node.UniNode;
import org.comroid.util.StreamOPs;

import java.util.*;
import java.util.concurrent.CompletableFuture;
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
                                        return updateGlobalCommand(cmd, def);
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
                                        return updateGuildCommand(guildId, cmd, def);
                                    })
                                    .filter(Objects::nonNull)
                            , // create nonexisting
                            globalDefinitions.stream()
                                    .filter(def -> !existingNames.contains(def.getName()))
                                    .map(def -> registerGuildCommand(guildId, def))
                    ).toArray(CompletableFuture<?>[]::new);
                }).thenCompose(CompletableFuture::allOf);
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
        // todo
    }
}

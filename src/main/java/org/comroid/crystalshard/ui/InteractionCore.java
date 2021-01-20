package org.comroid.crystalshard.ui;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.Context;
import org.comroid.crystalshard.DiscordBotBase;
import org.comroid.crystalshard.entity.command.Command;
import org.comroid.crystalshard.entity.guild.Guild;
import org.comroid.crystalshard.gateway.event.dispatch.interaction.InteractionCreateEvent;
import org.comroid.crystalshard.rest.Endpoint;
import org.comroid.restless.REST;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class InteractionCore implements Context {
    private static final Logger logger = LogManager.getLogger();
    private final DiscordBotBase bot;
    private final CommandSetup config;

    @Override
    public ContextualProvider getUnderlyingContextualProvider() {
        return bot;
    }

    public InteractionCore(DiscordBotBase bot, CommandSetup config) {
        this.bot = bot;
        this.config = config;

        bot.getEventPipeline()
                .flatMap(InteractionCreateEvent.class)
                .forEach(this::handleInteraction);

        requestGlobalCommands().exceptionally(exceptionLogger(logger, Level.ERROR, "Error fetching Commands", false));
    }

    public CompletableFuture<List<Command>> requestGlobalCommands() {
        return getBot().newRequest(
                REST.Method.GET,
                Endpoint.APPLICATION_COMMANDS_GLOBAL.complete(getBot().getOwnID()))
                .thenApply(array -> array.streamNodes()
                        .map(data -> Command.resolve(this, data))
                        .collect(Collectors.toList()))
                .thenApply(Collections::unmodifiableList);
    }

    public CompletableFuture<List<Command>> requestGuildCommands(Guild guild) {
        return getBot().newRequest(
                REST.Method.GET,
                Endpoint.APPLICATION_COMMANDS_GUILD.complete(getBot().getOwnID(), guild.getID()))
                .thenApply(array -> array.streamNodes()
                        .map(data -> Command.resolve(this, data))
                        .collect(Collectors.toList()))
                .thenApply(Collections::unmodifiableList);
    }

    private void handleInteraction(InteractionCreateEvent event) {
        // todo
    }
}

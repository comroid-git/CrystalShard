package org.comroid.crystalshard.ui;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.Context;
import org.comroid.crystalshard.DiscordBotBase;
import org.comroid.crystalshard.gateway.event.dispatch.interaction.InteractionCreateEvent;

public class InteractionCore implements Context {
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
    }

    private void handleInteraction(InteractionCreateEvent event) {
        // todo
    }
}

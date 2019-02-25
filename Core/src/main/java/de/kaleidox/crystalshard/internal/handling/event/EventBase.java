package de.kaleidox.crystalshard.internal.handling.event;

import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.api.handling.event.Event;
import de.kaleidox.crystalshard.internal.DiscordInternal;

public abstract class EventBase implements Event {
    private final DiscordInternal discord;

    public EventBase(DiscordInternal discordInternal) {
        this.discord = discordInternal;
    }

    // Override Methods
    @Override
    public Discord getDiscord() {
        return discord;
    }
}

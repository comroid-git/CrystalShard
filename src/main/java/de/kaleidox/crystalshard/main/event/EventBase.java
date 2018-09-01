package de.kaleidox.crystalshard.main.event;

import de.kaleidox.crystalshard.internal.DiscordInternal;

public abstract class EventBase {
    private final DiscordInternal discord;

    public EventBase(DiscordInternal discordInternal) {
        this.discord = discordInternal;
    }
}

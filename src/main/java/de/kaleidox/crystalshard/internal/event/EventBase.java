package de.kaleidox.crystalshard.internal.event;

import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.event.types.AttachingEvent;

public abstract class EventBase {
    private final DiscordInternal discord;
    private final AttachingEvent eventType;

    public EventBase(DiscordInternal discordInternal, AttachingEvent eventType) {
        this.discord = discordInternal;
        this.eventType = eventType;
    }

    public Discord getDiscord() {
        return discord;
    }

    public AttachingEvent getEventType() {
        return eventType;
    }
}

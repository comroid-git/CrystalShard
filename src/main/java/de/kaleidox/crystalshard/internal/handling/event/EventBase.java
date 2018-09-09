package de.kaleidox.crystalshard.internal.handling.event;

import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.handling.event.Event;
import de.kaleidox.crystalshard.main.handling.types.AttachingEvent;

public abstract class EventBase implements Event {
    private final DiscordInternal discord;
    private final AttachingEvent eventType;

    public EventBase(DiscordInternal discordInternal,
                     AttachingEvent eventType) {
        this.discord = discordInternal;
        this.eventType = eventType;
    }

    @Override
    public Discord getDiscord() {
        return discord;
    }

    @Override
    public AttachingEvent getEventType() {
        return eventType;
    }
}

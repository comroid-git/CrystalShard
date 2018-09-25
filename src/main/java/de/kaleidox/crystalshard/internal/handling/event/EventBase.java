package de.kaleidox.crystalshard.internal.handling.event;

import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.handling.event.Event;

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

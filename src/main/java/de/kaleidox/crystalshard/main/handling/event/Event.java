package de.kaleidox.crystalshard.main.handling.event;

import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.handling.types.AttachingEvent;

public interface Event {
    Discord getDiscord();

    AttachingEvent getEventType();
}

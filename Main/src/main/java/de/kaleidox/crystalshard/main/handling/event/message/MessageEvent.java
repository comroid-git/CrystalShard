package de.kaleidox.crystalshard.main.handling.event.message;

import de.kaleidox.crystalshard.main.handling.event.Event;
import de.kaleidox.crystalshard.main.items.message.Message;

public interface MessageEvent extends Event {
    default long getMessageId() {
        return getMessage().getId();
    }

    Message getMessage();

    default boolean isPrivate() {
        return getMessage().isPrivate();
    }
}

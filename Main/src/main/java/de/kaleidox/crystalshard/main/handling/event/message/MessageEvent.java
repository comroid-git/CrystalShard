package de.kaleidox.crystalshard.main.handling.event.message;

import de.kaleidox.crystalshard.main.handling.event.Event;
import de.kaleidox.crystalshard.main.items.message.Message;

public interface MessageEvent extends Event {
    Message getMessage();

    default long getMessageId() {
        return getMessage().getId();
    }

    default boolean isPrivate() {
        return getMessage().isPrivate();
    }
}

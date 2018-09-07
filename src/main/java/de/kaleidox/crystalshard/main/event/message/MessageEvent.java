package de.kaleidox.crystalshard.main.event.message;

import de.kaleidox.crystalshard.main.items.message.Message;

public interface MessageEvent {
    Message getMessage();

    default long getMessageId() {
        return getMessage().getId();
    }
}

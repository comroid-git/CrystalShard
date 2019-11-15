package de.comroid.crystalshard.api.event.multipart.message;

import de.comroid.crystalshard.api.entity.message.Message;
import de.comroid.crystalshard.api.event.multipart.APIEvent;

public interface MessageEvent extends APIEvent {
    Message getMessage();
}

package org.comroid.crystalshard.api.event.multipart.message;

import org.comroid.crystalshard.api.entity.message.Message;
import org.comroid.crystalshard.api.event.multipart.APIEvent;

public interface MessageEvent extends APIEvent {
    Message getMessage();
}

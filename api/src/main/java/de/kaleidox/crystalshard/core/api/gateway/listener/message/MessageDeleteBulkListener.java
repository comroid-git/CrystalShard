package de.kaleidox.crystalshard.core.api.gateway.listener.message;

import de.kaleidox.crystalshard.core.api.gateway.event.message.MessageDeleteBulkEvent;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListener;

@FunctionalInterface
public interface MessageDeleteBulkListener extends GatewayListener<MessageDeleteBulkEvent> {
    void onMessageDeleteBulk(MessageDeleteBulkEvent event);
}

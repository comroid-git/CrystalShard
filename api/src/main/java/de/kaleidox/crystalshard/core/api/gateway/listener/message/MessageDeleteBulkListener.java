package de.kaleidox.crystalshard.core.api.gateway.listener.message;

import de.kaleidox.crystalshard.core.api.gateway.event.message.MessageDeleteBulkEvent;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListener;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListenerManager;

public interface MessageDeleteBulkListener extends GatewayListener<MessageDeleteBulkEvent> {
    interface Manager extends GatewayListenerManager<MessageDeleteBulkListener> {
    }
}

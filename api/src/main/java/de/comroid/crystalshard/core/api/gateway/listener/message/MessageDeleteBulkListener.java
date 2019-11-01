package de.comroid.crystalshard.core.api.gateway.listener.message;

import de.comroid.crystalshard.core.api.gateway.event.message.MessageDeleteBulkEvent;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListenerManager;

public interface MessageDeleteBulkListener extends GatewayListener<MessageDeleteBulkEvent> {
    interface Manager extends GatewayListenerManager<MessageDeleteBulkListener> {
    }
}

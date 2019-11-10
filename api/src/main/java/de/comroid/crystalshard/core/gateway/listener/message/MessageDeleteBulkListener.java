package de.comroid.crystalshard.core.gateway.listener.message;

import de.comroid.crystalshard.core.gateway.event.MESSAGE_DELETE_BULK;
import de.comroid.crystalshard.core.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.gateway.listener.GatewayListenerManager;

public interface MessageDeleteBulkListener extends GatewayListener<MESSAGE_DELETE_BULK> {
    interface Manager extends GatewayListenerManager<MessageDeleteBulkListener> {
    }
}

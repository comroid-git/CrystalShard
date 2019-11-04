package de.comroid.crystalshard.core.api.gateway.listener.message;

import de.comroid.crystalshard.core.api.gateway.event.MESSAGE_DELETE_BULK;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListenerManager;

public interface MessageDeleteBulkListener extends GatewayListener<MESSAGE_DELETE_BULK> {
    interface Manager extends GatewayListenerManager<MessageDeleteBulkListener> {
    }
}

package de.comroid.crystalshard.core.gateway.listener.message;

import de.comroid.crystalshard.core.gateway.event.MESSAGE_DELETE;
import de.comroid.crystalshard.core.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.gateway.listener.GatewayListenerManager;

public interface MessageDeleteListener extends GatewayListener<MESSAGE_DELETE> {
    interface Manager extends GatewayListenerManager<MessageDeleteListener> {
    }
}

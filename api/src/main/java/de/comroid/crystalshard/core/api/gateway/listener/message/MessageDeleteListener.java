package de.comroid.crystalshard.core.api.gateway.listener.message;

import de.comroid.crystalshard.core.api.gateway.event.MESSAGE_DELETE;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListenerManager;

public interface MessageDeleteListener extends GatewayListener<MESSAGE_DELETE> {
    interface Manager extends GatewayListenerManager<MessageDeleteListener> {
    }
}

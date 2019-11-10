package de.comroid.crystalshard.core.gateway.listener.message;

import de.comroid.crystalshard.core.gateway.event.MESSAGE_UPDATE;
import de.comroid.crystalshard.core.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.gateway.listener.GatewayListenerManager;

public interface MessageUpdateListener extends GatewayListener<MESSAGE_UPDATE> {
    interface Manager extends GatewayListenerManager<MessageUpdateListener> {
    }
}

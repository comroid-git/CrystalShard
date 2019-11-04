package de.comroid.crystalshard.core.api.gateway.listener.message;

import de.comroid.crystalshard.core.api.gateway.event.MESSAGE_UPDATE;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListenerManager;

public interface MessageUpdateListener extends GatewayListener<MESSAGE_UPDATE> {
    interface Manager extends GatewayListenerManager<MessageUpdateListener> {
    }
}

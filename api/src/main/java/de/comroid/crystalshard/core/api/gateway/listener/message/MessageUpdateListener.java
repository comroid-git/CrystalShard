package de.comroid.crystalshard.core.api.gateway.listener.message;

import de.comroid.crystalshard.core.api.gateway.event.message.MessageUpdateEvent;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListenerManager;

public interface MessageUpdateListener extends GatewayListener<MessageUpdateEvent> {
    interface Manager extends GatewayListenerManager<MessageUpdateListener> {
    }
}

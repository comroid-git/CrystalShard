package de.comroid.crystalshard.core.api.gateway.listener.message;

import de.comroid.crystalshard.core.api.gateway.event.message.MessageDeleteEvent;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListenerManager;

public interface MessageDeleteListener extends GatewayListener<MessageDeleteEvent> {
    interface Manager extends GatewayListenerManager<MessageDeleteListener> {
    }
}

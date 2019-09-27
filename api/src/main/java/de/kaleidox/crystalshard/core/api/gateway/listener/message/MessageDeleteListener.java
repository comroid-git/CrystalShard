package de.kaleidox.crystalshard.core.api.gateway.listener.message;

import de.kaleidox.crystalshard.core.api.gateway.event.message.MessageDeleteEvent;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListener;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListenerManager;

public interface MessageDeleteListener extends GatewayListener<MessageDeleteEvent> {
    interface Manager extends GatewayListenerManager<MessageDeleteListener> {
    }
}

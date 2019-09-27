package de.kaleidox.crystalshard.core.api.gateway.listener.message;

import de.kaleidox.crystalshard.core.api.gateway.event.message.MessageCreateEvent;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListener;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListenerManager;

public interface MessageCreateListener extends GatewayListener<MessageCreateEvent> {
    interface Manager extends GatewayListenerManager<MessageCreateListener> {
    }
}

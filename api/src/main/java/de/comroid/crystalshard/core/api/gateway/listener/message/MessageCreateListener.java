package de.comroid.crystalshard.core.api.gateway.listener.message;

import de.comroid.crystalshard.core.api.gateway.event.message.MessageCreateEvent;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListenerManager;

public interface MessageCreateListener extends GatewayListener<MessageCreateEvent> {
    interface Manager extends GatewayListenerManager<MessageCreateListener> {
    }
}

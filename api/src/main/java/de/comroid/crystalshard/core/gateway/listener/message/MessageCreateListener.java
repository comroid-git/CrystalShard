package de.comroid.crystalshard.core.gateway.listener.message;

import de.comroid.crystalshard.core.gateway.event.MESSAGE_CREATE;
import de.comroid.crystalshard.core.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.gateway.listener.GatewayListenerManager;

public interface MessageCreateListener extends GatewayListener<MESSAGE_CREATE> {
    interface Manager extends GatewayListenerManager<MessageCreateListener> {
    }
}

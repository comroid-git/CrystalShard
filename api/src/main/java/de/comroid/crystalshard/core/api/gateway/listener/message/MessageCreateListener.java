package de.comroid.crystalshard.core.api.gateway.listener.message;

import de.comroid.crystalshard.core.api.gateway.event.MESSAGE_CREATE;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListenerManager;

public interface MessageCreateListener extends GatewayListener<MESSAGE_CREATE> {
    interface Manager extends GatewayListenerManager<MessageCreateListener> {
    }
}

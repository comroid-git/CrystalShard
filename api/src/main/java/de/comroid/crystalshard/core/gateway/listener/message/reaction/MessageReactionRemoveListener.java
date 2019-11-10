package de.comroid.crystalshard.core.gateway.listener.message.reaction;

import de.comroid.crystalshard.core.gateway.event.MessageReactionRemoveEvent;
import de.comroid.crystalshard.core.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.gateway.listener.GatewayListenerManager;

public interface MessageReactionRemoveListener extends GatewayListener<MessageReactionRemoveEvent> {
    interface Manager extends GatewayListenerManager<MessageReactionRemoveListener> {
    }
}

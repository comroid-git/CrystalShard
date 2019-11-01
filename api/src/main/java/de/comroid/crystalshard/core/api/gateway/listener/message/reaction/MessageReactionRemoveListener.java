package de.comroid.crystalshard.core.api.gateway.listener.message.reaction;

import de.comroid.crystalshard.core.api.gateway.event.message.reaction.MessageReactionRemoveEvent;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListenerManager;

public interface MessageReactionRemoveListener extends GatewayListener<MessageReactionRemoveEvent> {
    interface Manager extends GatewayListenerManager<MessageReactionRemoveListener> {
    }
}

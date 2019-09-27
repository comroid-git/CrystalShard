package de.kaleidox.crystalshard.core.api.gateway.listener.message.reaction;

import de.kaleidox.crystalshard.core.api.gateway.event.message.reaction.MessageReactionRemoveEvent;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListener;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListenerManager;

public interface MessageReactionRemoveListener extends GatewayListener<MessageReactionRemoveEvent> {
    interface Manager extends GatewayListenerManager<MessageReactionRemoveListener> {
    }
}

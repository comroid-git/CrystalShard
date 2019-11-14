package de.comroid.crystalshard.core.gateway.listener.message.reaction;

import de.comroid.crystalshard.core.gateway.event.MESSAGE_REACTION_REMOVE;
import de.comroid.crystalshard.core.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.gateway.listener.GatewayListenerManager;

public interface MessageReactionRemoveListener extends GatewayListener<MESSAGE_REACTION_REMOVE> {
    interface Manager extends GatewayListenerManager<MessageReactionRemoveListener> {
    }
}

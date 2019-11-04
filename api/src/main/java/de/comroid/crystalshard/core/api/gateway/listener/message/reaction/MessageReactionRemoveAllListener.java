package de.comroid.crystalshard.core.api.gateway.listener.message.reaction;

import de.comroid.crystalshard.core.api.gateway.event.MESSAGE_REACTION_REMOVE_ALL;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListenerManager;

public interface MessageReactionRemoveAllListener extends GatewayListener<MESSAGE_REACTION_REMOVE_ALL> {
    interface Manager extends GatewayListenerManager<MessageReactionRemoveAllListener> {
    }
}

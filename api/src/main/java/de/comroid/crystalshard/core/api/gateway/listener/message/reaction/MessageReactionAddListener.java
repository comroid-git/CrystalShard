package de.comroid.crystalshard.core.api.gateway.listener.message.reaction;

import de.comroid.crystalshard.core.api.gateway.event.MESSAGE_REACTION_ADD;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListenerManager;

public interface MessageReactionAddListener extends GatewayListener<MESSAGE_REACTION_ADD> {
    interface Manager extends GatewayListenerManager<MessageReactionAddListener> {
    }
}

package de.comroid.crystalshard.core.api.gateway.listener.message.reaction;

import de.comroid.crystalshard.core.api.gateway.event.message.reaction.MessageReactionAddEvent;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListenerManager;

public interface MessageReactionAddListener extends GatewayListener<MessageReactionAddEvent> {
    interface Manager extends GatewayListenerManager<MessageReactionAddListener> {
    }
}

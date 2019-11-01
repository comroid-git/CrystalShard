package de.comroid.crystalshard.core.api.gateway.listener.message.reaction;

import de.comroid.crystalshard.core.api.gateway.event.message.reaction.MessageReactionRemoveAllEvent;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListenerManager;

public interface MessageReactionRemoveAllListener extends GatewayListener<MessageReactionRemoveAllEvent> {
    interface Manager extends GatewayListenerManager<MessageReactionRemoveAllListener> {
    }
}

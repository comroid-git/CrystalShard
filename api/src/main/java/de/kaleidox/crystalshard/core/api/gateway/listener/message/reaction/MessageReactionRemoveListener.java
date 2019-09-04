package de.kaleidox.crystalshard.core.api.gateway.listener.message.reaction;

import de.kaleidox.crystalshard.core.api.gateway.event.message.reaction.MessageReactionRemoveEvent;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListener;

@FunctionalInterface
public interface MessageReactionRemoveListener extends GatewayListener<MessageReactionRemoveEvent> {
    void onMessageReactionRemove(MessageReactionRemoveEvent event);
}

package de.kaleidox.crystalshard.core.api.gateway.listener.message.reaction;

import de.kaleidox.crystalshard.core.api.gateway.event.message.reaction.MessageReactionRemoveAllEvent;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListener;

@FunctionalInterface
public interface MessageReactionRemoveAllListener extends GatewayListener<MessageReactionRemoveAllEvent> {
    void onMessageReactionRemoveAll(MessageReactionRemoveAllEvent event);
}

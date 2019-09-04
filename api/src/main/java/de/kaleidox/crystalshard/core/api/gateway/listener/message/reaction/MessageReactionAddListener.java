package de.kaleidox.crystalshard.core.api.gateway.listener.message.reaction;

import de.kaleidox.crystalshard.core.api.gateway.event.message.reaction.MessageReactionAddEvent;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListener;

@FunctionalInterface
public interface MessageReactionAddListener extends GatewayListener<MessageReactionAddEvent> {
    void onMessageReactionAdd(MessageReactionAddEvent event);
}

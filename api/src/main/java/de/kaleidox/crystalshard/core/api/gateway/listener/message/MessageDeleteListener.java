package de.kaleidox.crystalshard.core.api.gateway.listener.message;

import de.kaleidox.crystalshard.core.api.gateway.event.message.MessageDeleteEvent;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListener;

@FunctionalInterface
public interface MessageDeleteListener extends GatewayListener<MessageDeleteEvent> {
    void onMessageDelete(MessageDeleteEvent event);
}

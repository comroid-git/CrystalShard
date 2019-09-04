package de.kaleidox.crystalshard.core.api.gateway.listener.message;

import de.kaleidox.crystalshard.core.api.gateway.event.message.MessageUpdateEvent;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListener;

@FunctionalInterface
public interface MessageUpdateListener extends GatewayListener<MessageUpdateEvent> {
    void onMessageUpdate(MessageUpdateEvent event);
}

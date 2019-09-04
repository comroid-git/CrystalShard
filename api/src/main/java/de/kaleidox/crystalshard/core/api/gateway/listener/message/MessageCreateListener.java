package de.kaleidox.crystalshard.core.api.gateway.listener.message;

import de.kaleidox.crystalshard.core.api.gateway.event.message.MessageCreateEvent;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListener;

@FunctionalInterface
public interface MessageCreateListener extends GatewayListener<MessageCreateEvent> {
    void onMessageCreate(MessageCreateEvent event);
}

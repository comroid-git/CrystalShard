package de.kaleidox.crystalshard.core.api.gateway.listener.channel.type;

import de.kaleidox.crystalshard.core.api.gateway.event.channel.type.TypingStartEvent;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListener;

@FunctionalInterface
public interface TypingStartListener extends GatewayListener<TypingStartEvent> {
    void onTypingStart(TypingStartEvent event);
}

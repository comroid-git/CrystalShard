package de.kaleidox.crystalshard.core.api.gateway.listener.common;

import de.kaleidox.crystalshard.core.api.gateway.event.common.InvalidSessionEvent;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListener;

@FunctionalInterface
public interface InvalidSessionListener extends GatewayListener<InvalidSessionEvent> {
    void onInvalidSession(InvalidSessionEvent event);
}

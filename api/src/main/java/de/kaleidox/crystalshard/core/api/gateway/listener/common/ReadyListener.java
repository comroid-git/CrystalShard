package de.kaleidox.crystalshard.core.api.gateway.listener.common;

import de.kaleidox.crystalshard.core.api.gateway.event.common.ReadyEvent;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListener;

@FunctionalInterface
public interface ReadyListener extends GatewayListener<ReadyEvent> {
    void onReady(ReadyEvent event);
}

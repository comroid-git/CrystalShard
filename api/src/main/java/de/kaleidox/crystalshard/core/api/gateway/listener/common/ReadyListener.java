package de.kaleidox.crystalshard.core.api.gateway.listener.common;

import de.kaleidox.crystalshard.core.api.gateway.event.common.ReadyEvent;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListener;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListenerManager;

public interface ReadyListener extends GatewayListener<ReadyEvent> {
    interface Manager extends GatewayListenerManager<ReadyListener> {
    }
}

package de.comroid.crystalshard.core.gateway.listener.common;

import de.comroid.crystalshard.core.gateway.event.READY;
import de.comroid.crystalshard.core.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.gateway.listener.GatewayListenerManager;

public interface ReadyListener extends GatewayListener<READY> {
    interface Manager extends GatewayListenerManager<ReadyListener> {
    }
}

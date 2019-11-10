package de.comroid.crystalshard.core.gateway.listener.common;

import de.comroid.crystalshard.core.gateway.event.INVALID_SESSION;
import de.comroid.crystalshard.core.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.gateway.listener.GatewayListenerManager;

public interface InvalidSessionListener extends GatewayListener<INVALID_SESSION> {
    interface Manager extends GatewayListenerManager<InvalidSessionListener> {
    }
}

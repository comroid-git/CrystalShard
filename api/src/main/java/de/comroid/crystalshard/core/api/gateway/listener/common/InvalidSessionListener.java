package de.comroid.crystalshard.core.api.gateway.listener.common;

import de.comroid.crystalshard.core.api.gateway.event.INVALID_SESSION;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListenerManager;

public interface InvalidSessionListener extends GatewayListener<INVALID_SESSION> {
    interface Manager extends GatewayListenerManager<InvalidSessionListener> {
    }
}

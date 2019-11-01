package de.comroid.crystalshard.core.api.gateway.listener.common;

import de.comroid.crystalshard.core.api.gateway.event.common.InvalidSessionEvent;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListenerManager;

public interface InvalidSessionListener extends GatewayListener<InvalidSessionEvent> {
    interface Manager extends GatewayListenerManager<InvalidSessionListener> {
    }
}

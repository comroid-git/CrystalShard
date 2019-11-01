package de.comroid.crystalshard.core.api.gateway.listener.common;

import de.comroid.crystalshard.core.api.gateway.event.common.HelloEvent;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListenerManager;

public interface HelloListener extends GatewayListener<HelloEvent> {
    interface Manager extends GatewayListenerManager<HelloListener> {
    }
}

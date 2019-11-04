package de.comroid.crystalshard.core.api.gateway.listener.common;

import de.comroid.crystalshard.core.api.gateway.event.HELLO;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListenerManager;

public interface HelloListener extends GatewayListener<HELLO> {
    interface Manager extends GatewayListenerManager<HelloListener> {
    }
}

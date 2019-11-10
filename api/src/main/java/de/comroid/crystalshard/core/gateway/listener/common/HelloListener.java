package de.comroid.crystalshard.core.gateway.listener.common;

import de.comroid.crystalshard.core.gateway.event.HELLO;
import de.comroid.crystalshard.core.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.gateway.listener.GatewayListenerManager;

public interface HelloListener extends GatewayListener<HELLO> {
    interface Manager extends GatewayListenerManager<HelloListener> {
    }
}

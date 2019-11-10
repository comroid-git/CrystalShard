package de.comroid.crystalshard.core.gateway.listener.common;

import de.comroid.crystalshard.core.gateway.event.RESUMED;
import de.comroid.crystalshard.core.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.gateway.listener.GatewayListenerManager;

public interface ResumedListener extends GatewayListener<RESUMED> {
    interface Manager extends GatewayListenerManager<ResumedListener> {
    }
}

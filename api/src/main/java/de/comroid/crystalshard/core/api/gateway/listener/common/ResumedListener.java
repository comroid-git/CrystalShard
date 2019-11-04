package de.comroid.crystalshard.core.api.gateway.listener.common;

import de.comroid.crystalshard.core.api.gateway.event.RESUMED;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListenerManager;

public interface ResumedListener extends GatewayListener<RESUMED> {
    interface Manager extends GatewayListenerManager<ResumedListener> {
    }
}

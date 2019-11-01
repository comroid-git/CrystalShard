package de.comroid.crystalshard.core.api.gateway.listener.common;

import de.comroid.crystalshard.core.api.gateway.event.common.ResumedEvent;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListenerManager;

public interface ResumedListener extends GatewayListener<ResumedEvent> {
    interface Manager extends GatewayListenerManager<ResumedListener> {
    }
}

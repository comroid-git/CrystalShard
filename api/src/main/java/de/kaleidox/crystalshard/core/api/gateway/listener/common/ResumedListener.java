package de.kaleidox.crystalshard.core.api.gateway.listener.common;

import de.kaleidox.crystalshard.core.api.gateway.event.common.ResumedEvent;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListener;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListenerManager;

public interface ResumedListener extends GatewayListener<ResumedEvent> {
    interface Manager extends GatewayListenerManager<ResumedListener> {
    }
}

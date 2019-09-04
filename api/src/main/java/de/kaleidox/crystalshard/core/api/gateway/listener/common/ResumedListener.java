package de.kaleidox.crystalshard.core.api.gateway.listener.common;

import de.kaleidox.crystalshard.core.api.gateway.event.common.ResumedEvent;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListener;

@FunctionalInterface
public interface ResumedListener extends GatewayListener<ResumedEvent> {
    void onResumed(ResumedEvent event);
}

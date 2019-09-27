package de.kaleidox.crystalshard.core.api.gateway.event.common;

import de.kaleidox.crystalshard.core.api.gateway.event.GatewayEvent;
import de.kaleidox.crystalshard.core.api.gateway.listener.common.HelloListener;
import de.kaleidox.crystalshard.util.annotation.ManagedBy;

@ManagedBy(HelloListener.Manager.class)
public interface HelloEvent extends GatewayEvent {
    String NAME = "HELLO";

    int getHeartbeatInterval();
}

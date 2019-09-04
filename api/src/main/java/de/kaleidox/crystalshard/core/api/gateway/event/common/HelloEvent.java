package de.kaleidox.crystalshard.core.api.gateway.event.common;

import de.kaleidox.crystalshard.core.api.gateway.event.GatewayEvent;

public interface HelloEvent extends GatewayEvent {
    String NAME = "HELLO";

    int getHeartbeatInterval();
}

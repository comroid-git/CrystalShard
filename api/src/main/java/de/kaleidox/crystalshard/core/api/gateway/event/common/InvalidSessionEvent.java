package de.kaleidox.crystalshard.core.api.gateway.event.common;

// https://discordapp.com/developers/docs/topics/gateway#invalid-session

import de.kaleidox.crystalshard.core.api.gateway.event.GatewayEvent;

public interface InvalidSessionEvent extends GatewayEvent {
    String NAME = "INVALID_SESSION";

    boolean isResumable();
}

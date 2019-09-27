package de.kaleidox.crystalshard.core.api.gateway.event.common;

// https://discordapp.com/developers/docs/topics/gateway#invalid-session

import de.kaleidox.crystalshard.core.api.gateway.event.GatewayEvent;
import de.kaleidox.crystalshard.core.api.gateway.listener.common.InvalidSessionListener;
import de.kaleidox.crystalshard.util.annotation.ManagedBy;

@ManagedBy(InvalidSessionListener.Manager.class)
public interface InvalidSessionEvent extends GatewayEvent {
    String NAME = "INVALID_SESSION";

    boolean isResumable();
}

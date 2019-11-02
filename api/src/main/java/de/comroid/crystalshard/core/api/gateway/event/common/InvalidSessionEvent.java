package de.comroid.crystalshard.core.api.gateway.event.common;

// https://discordapp.com/developers/docs/topics/gateway#invalid-session

import de.comroid.crystalshard.core.api.gateway.event.GatewayEvent;
import de.comroid.crystalshard.core.api.gateway.listener.common.InvalidSessionListener;
import de.comroid.crystalshard.util.annotation.InitializedBy;

public interface InvalidSessionEvent extends GatewayEvent {
    String NAME = "INVALID_SESSION";

    boolean isResumable();
}

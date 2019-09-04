package de.kaleidox.crystalshard.core.api.gateway.event.common;

// https://discordapp.com/developers/docs/topics/gateway#resumed

import de.kaleidox.crystalshard.core.api.gateway.event.GatewayEvent;

public interface ResumedEvent extends GatewayEvent {
    String NAME = "RESUMED";
}

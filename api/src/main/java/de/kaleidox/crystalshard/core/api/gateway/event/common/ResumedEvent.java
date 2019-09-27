package de.kaleidox.crystalshard.core.api.gateway.event.common;

// https://discordapp.com/developers/docs/topics/gateway#resumed

import de.kaleidox.crystalshard.core.api.gateway.event.GatewayEvent;
import de.kaleidox.crystalshard.core.api.gateway.listener.common.ResumedListener;
import de.kaleidox.crystalshard.util.annotation.ManagedBy;

@ManagedBy(ResumedListener.Manager.class)
public interface ResumedEvent extends GatewayEvent {
    String NAME = "RESUMED";
}

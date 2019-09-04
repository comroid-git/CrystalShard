package de.kaleidox.crystalshard.core.api.gateway.event.message;

// https://discordapp.com/developers/docs/topics/gateway#message-delete

import de.kaleidox.crystalshard.core.api.gateway.event.GatewayEvent;

public interface MessageDeleteEvent extends GatewayEvent {
    String NAME = "MESSAGE_DELETE";
}

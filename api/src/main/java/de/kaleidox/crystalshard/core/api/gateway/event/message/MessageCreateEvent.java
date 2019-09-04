package de.kaleidox.crystalshard.core.api.gateway.event.message;

// https://discordapp.com/developers/docs/topics/gateway#message-create

import de.kaleidox.crystalshard.api.entity.message.Message;
import de.kaleidox.crystalshard.core.api.gateway.event.GatewayEvent;

public interface MessageCreateEvent extends GatewayEvent {
    String NAME = "MESSAGE_CREATE";

    Message getMessage();
}

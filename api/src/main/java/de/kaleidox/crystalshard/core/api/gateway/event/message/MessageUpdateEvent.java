package de.kaleidox.crystalshard.core.api.gateway.event.message;

// https://discordapp.com/developers/docs/topics/gateway#message-update

import de.kaleidox.crystalshard.api.entity.message.Message;
import de.kaleidox.crystalshard.core.api.gateway.event.GatewayEvent;
import de.kaleidox.crystalshard.core.api.gateway.listener.message.MessageUpdateListener;
import de.kaleidox.crystalshard.util.annotation.ManagedBy;

@ManagedBy(MessageUpdateListener.Manager.class)
public interface MessageUpdateEvent extends GatewayEvent {
    String NAME = "MESSAGE_UPDATE";

    Message getMessage();
}

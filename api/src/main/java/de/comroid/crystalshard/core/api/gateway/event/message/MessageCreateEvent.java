package de.comroid.crystalshard.core.api.gateway.event.message;

// https://discordapp.com/developers/docs/topics/gateway#message-create

import de.comroid.crystalshard.api.entity.message.Message;
import de.comroid.crystalshard.core.api.gateway.event.GatewayEvent;
import de.comroid.crystalshard.core.api.gateway.listener.message.MessageCreateListener;
import de.comroid.crystalshard.util.annotation.ManagedBy;

@ManagedBy(MessageCreateListener.Manager.class)
public interface MessageCreateEvent extends GatewayEvent {
    String NAME = "MESSAGE_CREATE";

    Message getMessage();
}

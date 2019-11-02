package de.comroid.crystalshard.core.api.gateway.event.message;

// https://discordapp.com/developers/docs/topics/gateway#message-update

import de.comroid.crystalshard.api.entity.message.Message;
import de.comroid.crystalshard.core.api.gateway.event.GatewayEvent;
import de.comroid.crystalshard.core.api.gateway.listener.message.MessageUpdateListener;
import de.comroid.crystalshard.util.annotation.InitializedBy;

public interface MessageUpdateEvent extends GatewayEvent {
    String NAME = "MESSAGE_UPDATE";

    Message getMessage();
}

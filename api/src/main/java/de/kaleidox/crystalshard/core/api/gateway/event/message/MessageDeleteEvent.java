package de.kaleidox.crystalshard.core.api.gateway.event.message;

// https://discordapp.com/developers/docs/topics/gateway#message-delete

import java.util.Optional;

import de.kaleidox.crystalshard.api.entity.channel.TextChannel;
import de.kaleidox.crystalshard.api.entity.guild.Guild;
import de.kaleidox.crystalshard.api.entity.message.Message;
import de.kaleidox.crystalshard.core.api.gateway.event.GatewayEvent;
import de.kaleidox.crystalshard.core.api.gateway.listener.message.MessageDeleteListener;
import de.kaleidox.crystalshard.util.annotation.ManagedBy;

@ManagedBy(MessageDeleteListener.Manager.class)
public interface MessageDeleteEvent extends GatewayEvent {
    String NAME = "MESSAGE_DELETE";

    Message getMessage();

    TextChannel getChannel();

    Optional<Guild> getGuild();
}

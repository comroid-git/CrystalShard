package de.comroid.crystalshard.core.api.gateway.event.message;

// https://discordapp.com/developers/docs/topics/gateway#message-delete

import java.util.Optional;

import de.comroid.crystalshard.api.entity.channel.TextChannel;
import de.comroid.crystalshard.api.entity.guild.Guild;
import de.comroid.crystalshard.api.entity.message.Message;
import de.comroid.crystalshard.core.api.gateway.event.GatewayEvent;
import de.comroid.crystalshard.core.api.gateway.listener.message.MessageDeleteListener;
import de.comroid.crystalshard.util.annotation.InitializedBy;

public interface MessageDeleteEvent extends GatewayEvent {
    String NAME = "MESSAGE_DELETE";

    Message getMessage();

    TextChannel getChannel();

    Optional<Guild> getGuild();
}

package de.comroid.crystalshard.core.api.gateway.event.message.reaction;

// https://discordapp.com/developers/docs/topics/gateway#message-reaction-remove-all

import java.util.Optional;

import de.comroid.crystalshard.api.entity.channel.TextChannel;
import de.comroid.crystalshard.api.entity.guild.Guild;
import de.comroid.crystalshard.api.entity.message.Message;
import de.comroid.crystalshard.core.api.gateway.event.GatewayEvent;
import de.comroid.crystalshard.core.api.gateway.listener.message.reaction.MessageReactionRemoveAllListener;
import de.comroid.crystalshard.util.annotation.ManagedBy;

@ManagedBy(MessageReactionRemoveAllListener.Manager.class)
public interface MessageReactionRemoveAllEvent extends GatewayEvent {
    String NAME = "MESSAGE_REACTION_REMOVE_ALL";

    TextChannel getChannel();

    Message getMessage();

    Optional<Guild> getGuild();
}

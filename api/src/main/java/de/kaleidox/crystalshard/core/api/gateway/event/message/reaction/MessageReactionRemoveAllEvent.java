package de.kaleidox.crystalshard.core.api.gateway.event.message.reaction;

// https://discordapp.com/developers/docs/topics/gateway#message-reaction-remove-all

import java.util.Optional;

import de.kaleidox.crystalshard.api.entity.channel.TextChannel;
import de.kaleidox.crystalshard.api.entity.guild.Guild;
import de.kaleidox.crystalshard.api.entity.message.Message;
import de.kaleidox.crystalshard.core.api.gateway.event.GatewayEvent;

public interface MessageReactionRemoveAllEvent extends GatewayEvent {
    String NAME = "MESSAGE_REACTION_REMOVE_ALL";

    TextChannel getChannel();

    Message getMessage();

    Optional<Guild> getGuild();
}

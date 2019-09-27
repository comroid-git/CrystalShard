package de.kaleidox.crystalshard.core.api.gateway.event.channel.type;

// https://discordapp.com/developers/docs/topics/gateway#typing-start

import java.time.Instant;
import java.util.Optional;

import de.kaleidox.crystalshard.api.entity.channel.TextChannel;
import de.kaleidox.crystalshard.api.entity.guild.Guild;
import de.kaleidox.crystalshard.api.entity.user.User;
import de.kaleidox.crystalshard.core.api.gateway.event.GatewayEvent;
import de.kaleidox.crystalshard.core.api.gateway.listener.channel.type.TypingStartListener;
import de.kaleidox.crystalshard.util.annotation.ManagedBy;

@ManagedBy(TypingStartListener.Manager.class)
public interface ChannelTypingStartEvent extends GatewayEvent {
    String NAME = "TYPING_START";

    TextChannel getChannel();

    Optional<Guild> getGuild();

    User getUser();

    Instant getStartedTimestamp();
}

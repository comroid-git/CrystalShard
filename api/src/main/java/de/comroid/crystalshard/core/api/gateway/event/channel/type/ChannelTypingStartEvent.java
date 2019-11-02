package de.comroid.crystalshard.core.api.gateway.event.channel.type;

// https://discordapp.com/developers/docs/topics/gateway#typing-start

import java.time.Instant;
import java.util.Optional;

import de.comroid.crystalshard.api.entity.channel.TextChannel;
import de.comroid.crystalshard.api.entity.guild.Guild;
import de.comroid.crystalshard.api.entity.user.User;
import de.comroid.crystalshard.core.api.gateway.event.GatewayEvent;
import de.comroid.crystalshard.core.api.gateway.listener.channel.type.TypingStartListener;
import de.comroid.crystalshard.util.annotation.InitializedBy;

public interface ChannelTypingStartEvent extends GatewayEvent {
    String NAME = "TYPING_START";

    TextChannel getChannel();

    Optional<Guild> getGuild();

    User getUser();

    Instant getStartedTimestamp();
}

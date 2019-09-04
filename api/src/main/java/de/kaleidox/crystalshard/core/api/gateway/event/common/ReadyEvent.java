package de.kaleidox.crystalshard.core.api.gateway.event.common;

// https://discordapp.com/developers/docs/topics/gateway#ready

import java.util.Collection;
import java.util.Optional;

import de.kaleidox.crystalshard.api.entity.channel.PrivateChannel;
import de.kaleidox.crystalshard.api.entity.guild.Guild;
import de.kaleidox.crystalshard.api.entity.user.User;
import de.kaleidox.crystalshard.core.api.gateway.event.GatewayEvent;

public interface ReadyEvent extends GatewayEvent {
    String NAME = "READY";

    int getGatewayProtocolVersion();

    User getYourself();

    Collection<PrivateChannel> getPrivateChannels();

    Collection<Guild> getGuilds();

    String getSessionID();

    Optional<int[]> getShardInformation();
}

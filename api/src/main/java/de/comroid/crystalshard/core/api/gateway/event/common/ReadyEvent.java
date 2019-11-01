package de.comroid.crystalshard.core.api.gateway.event.common;

// https://discordapp.com/developers/docs/topics/gateway#ready

import java.util.Collection;
import java.util.Optional;

import de.comroid.crystalshard.api.entity.channel.PrivateChannel;
import de.comroid.crystalshard.api.entity.guild.Guild;
import de.comroid.crystalshard.api.entity.user.User;
import de.comroid.crystalshard.core.api.gateway.event.GatewayEvent;
import de.comroid.crystalshard.core.api.gateway.listener.common.ReadyListener;
import de.comroid.crystalshard.util.annotation.ManagedBy;

@ManagedBy(ReadyListener.Manager.class)
public interface ReadyEvent extends GatewayEvent {
    String NAME = "READY";

    int getGatewayProtocolVersion();

    User getYourself();

    Collection<PrivateChannel> getPrivateChannels();

    Collection<Guild> getGuilds();

    String getSessionID();

    Optional<int[]> getShardInformation();
}

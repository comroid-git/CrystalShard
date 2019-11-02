package de.comroid.crystalshard.core.api.gateway.event.common;

// https://discordapp.com/developers/docs/topics/gateway#ready

import java.util.Collection;
import java.util.Optional;

import de.comroid.crystalshard.api.entity.channel.PrivateChannel;
import de.comroid.crystalshard.api.entity.guild.Guild;
import de.comroid.crystalshard.api.entity.user.User;
import de.comroid.crystalshard.api.model.user.Yourself;
import de.comroid.crystalshard.core.api.gateway.event.GatewayEvent;
import de.comroid.crystalshard.core.api.gateway.listener.common.ReadyListener;
import de.comroid.crystalshard.util.annotation.InitializedBy;

public interface ReadyEvent extends GatewayEvent {
    String NAME = "READY";

    int getGatewayProtocolVersion();

    Yourself getYourself();

    Collection<PrivateChannel> getPrivateChannels();

    Collection<Guild> getGuilds();

    String getSessionID();

    Optional<int[]> getShardInformation();
}

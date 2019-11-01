package de.comroid.crystalshard.core.gateway.event.common;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import de.comroid.crystalshard.abstraction.gateway.AbstractGatewayEvent;
import de.comroid.crystalshard.api.Discord;
import de.comroid.crystalshard.api.entity.channel.PrivateChannel;
import de.comroid.crystalshard.api.entity.guild.Guild;
import de.comroid.crystalshard.api.entity.user.User;
import de.comroid.crystalshard.core.annotation.JsonData;
import de.comroid.crystalshard.core.api.gateway.event.common.ReadyEvent;
import de.comroid.crystalshard.util.annotation.IntroducedBy;

import com.fasterxml.jackson.databind.JsonNode;

import static de.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;

@IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/topics/gateway#ready")
public class ReadyEventImpl extends AbstractGatewayEvent implements ReadyEvent {
    protected @JsonData("v") int gatewayProtocolVersion;
    protected @JsonData("user") User yourself;
    protected @JsonData(value = "private_channels", type = PrivateChannel.class) Collection<PrivateChannel> privateChannels;
    protected @JsonData(value = "guilds", type = Guild.class) Collection<Guild> unavailableGuilds;
    protected @JsonData("session_id") String sessionId;
    protected @JsonData("shard") int[] shardInformation;

    public ReadyEventImpl(Discord api, JsonNode data) {
        super(api, data);
    }

    @Override
    public int getGatewayProtocolVersion() {
        return gatewayProtocolVersion;
    }

    @Override
    public User getYourself() {
        return yourself;
    }

    @Override
    public Collection<PrivateChannel> getPrivateChannels() {
        return privateChannels;
    }

    @Override
    public Collection<Guild> getGuilds() {
        return Collections.unmodifiableCollection(unavailableGuilds);
    }

    @Override
    public String getSessionID() {
        return sessionId;
    }

    @Override
    public Optional<int[]> getShardInformation() {
        return Optional.ofNullable(shardInformation);
    }
}

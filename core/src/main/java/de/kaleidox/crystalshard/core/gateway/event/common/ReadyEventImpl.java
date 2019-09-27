package de.kaleidox.crystalshard.core.gateway.event.common;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import de.kaleidox.crystalshard.abstraction.gateway.AbstractGatewayEvent;
import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.api.entity.channel.PrivateChannel;
import de.kaleidox.crystalshard.api.entity.guild.Guild;
import de.kaleidox.crystalshard.api.entity.user.User;
import de.kaleidox.crystalshard.api.listener.model.ListenerAttachable;
import de.kaleidox.crystalshard.core.annotation.JsonData;
import de.kaleidox.crystalshard.core.api.gateway.event.common.ReadyEvent;
import de.kaleidox.crystalshard.util.annotation.IntroducedBy;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import static de.kaleidox.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;

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

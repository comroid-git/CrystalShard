package de.comroid.crystalshard.core.gateway.event.channel;

import de.comroid.crystalshard.abstraction.gateway.AbstractGatewayEvent;
import de.comroid.crystalshard.api.Discord;
import de.comroid.crystalshard.api.entity.channel.Channel;
import de.comroid.crystalshard.api.entity.channel.GuildChannel;
import de.comroid.crystalshard.core.annotation.JsonData;
import de.comroid.crystalshard.core.api.gateway.event.channel.ChannelUpdateEvent;
import de.comroid.crystalshard.util.annotation.IntroducedBy;

import com.fasterxml.jackson.databind.JsonNode;

import static de.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;

@IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/topics/gateway#channel-update")
public class ChannelUpdateEventImpl extends AbstractGatewayEvent implements ChannelUpdateEvent {
    protected @JsonData Channel updatedChannel;

    public ChannelUpdateEventImpl(Discord api, JsonNode data) {
        super(api, data);

        affects(updatedChannel);
        updatedChannel.as(GuildChannel.class)
                .map(GuildChannel::getGuild)
                .ifPresent(this::affects);
    }

    @Override
    public Channel getChannel() {
        return updatedChannel;
    }
}

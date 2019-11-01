package de.comroid.crystalshard.core.gateway.event.channel;

import de.comroid.crystalshard.abstraction.gateway.AbstractGatewayEvent;
import de.comroid.crystalshard.api.Discord;
import de.comroid.crystalshard.api.entity.channel.Channel;
import de.comroid.crystalshard.api.entity.channel.GuildChannel;
import de.comroid.crystalshard.core.annotation.JsonData;
import de.comroid.crystalshard.core.api.gateway.event.channel.ChannelDeleteEvent;
import de.comroid.crystalshard.util.annotation.IntroducedBy;

import com.fasterxml.jackson.databind.JsonNode;

import static de.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;

@IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/topics/gateway#channel-delete")
public class ChannelDeleteEventImpl extends AbstractGatewayEvent implements ChannelDeleteEvent {
    protected @JsonData Channel deletedChannel;

    public ChannelDeleteEventImpl(Discord api, JsonNode data) {
        super(api, data);

        affects(deletedChannel);
        deletedChannel.as(GuildChannel.class)
                .map(GuildChannel::getGuild)
                .ifPresent(this::affects);
    }

    @Override
    public Channel getChannel() {
        return deletedChannel;
    }
}

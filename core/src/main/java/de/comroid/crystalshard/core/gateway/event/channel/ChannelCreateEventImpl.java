package de.comroid.crystalshard.core.gateway.event.channel;

import de.comroid.crystalshard.abstraction.gateway.AbstractGatewayEvent;
import de.comroid.crystalshard.api.Discord;
import de.comroid.crystalshard.api.entity.channel.Channel;
import de.comroid.crystalshard.api.entity.channel.GuildChannel;
import de.comroid.crystalshard.core.annotation.JsonData;
import de.comroid.crystalshard.core.api.gateway.event.channel.ChannelCreateEvent;
import de.comroid.crystalshard.util.annotation.IntroducedBy;

import com.fasterxml.jackson.databind.JsonNode;

import static de.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;

@IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/topics/gateway#channel-create")
public class ChannelCreateEventImpl extends AbstractGatewayEvent implements ChannelCreateEvent {
    protected @JsonData Channel createdChannel;

    public ChannelCreateEventImpl(Discord api, JsonNode data) {
        super(api, data);

        createdChannel.as(GuildChannel.class)
                .ifPresent(gtc -> {
                    affects(gtc.getGuild());
                    affects(gtc);
                });
    }

    @Override
    public Channel getChannel() {
        return createdChannel;
    }
}

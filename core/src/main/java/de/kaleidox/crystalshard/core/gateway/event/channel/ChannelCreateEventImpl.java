package de.kaleidox.crystalshard.core.gateway.event.channel;

import de.kaleidox.crystalshard.abstraction.gateway.AbstractGatewayEvent;
import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.api.entity.channel.Channel;
import de.kaleidox.crystalshard.api.entity.channel.GuildChannel;
import de.kaleidox.crystalshard.api.listener.model.ListenerAttachable;
import de.kaleidox.crystalshard.core.annotation.JsonData;
import de.kaleidox.crystalshard.core.api.gateway.event.channel.ChannelCreateEvent;
import de.kaleidox.crystalshard.util.annotation.IntroducedBy;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import static de.kaleidox.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;

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

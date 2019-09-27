package de.kaleidox.crystalshard.core.api.gateway.event.channel;

// https://discordapp.com/developers/docs/topics/gateway#channel-create

import de.kaleidox.crystalshard.api.entity.channel.Channel;
import de.kaleidox.crystalshard.core.api.gateway.event.GatewayEvent;
import de.kaleidox.crystalshard.core.api.gateway.listener.channel.ChannelCreateListener;
import de.kaleidox.crystalshard.util.annotation.ManagedBy;

@ManagedBy(ChannelCreateListener.Manager.class)
public interface ChannelCreateEvent extends GatewayEvent {
    String NAME = "CHANNEL_CREATE";

    Channel getChannel();
}

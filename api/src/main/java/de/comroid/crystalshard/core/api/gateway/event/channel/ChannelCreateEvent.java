package de.comroid.crystalshard.core.api.gateway.event.channel;

// https://discordapp.com/developers/docs/topics/gateway#channel-create

import de.comroid.crystalshard.api.entity.channel.Channel;
import de.comroid.crystalshard.core.api.gateway.event.GatewayEvent;
import de.comroid.crystalshard.core.api.gateway.listener.channel.ChannelCreateListener;
import de.comroid.crystalshard.util.annotation.ManagedBy;

@ManagedBy(ChannelCreateListener.Manager.class)
public interface ChannelCreateEvent extends GatewayEvent {
    String NAME = "CHANNEL_CREATE";

    Channel getChannel();
}

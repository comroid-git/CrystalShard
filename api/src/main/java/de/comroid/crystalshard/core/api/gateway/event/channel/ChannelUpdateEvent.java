package de.comroid.crystalshard.core.api.gateway.event.channel;

// https://discordapp.com/developers/docs/topics/gateway#channel-update

import de.comroid.crystalshard.api.entity.channel.Channel;
import de.comroid.crystalshard.core.api.gateway.event.GatewayEvent;
import de.comroid.crystalshard.core.api.gateway.listener.channel.ChannelUpdateListener;
import de.comroid.crystalshard.util.annotation.ManagedBy;

@ManagedBy(ChannelUpdateListener.Manager.class)
public interface ChannelUpdateEvent extends GatewayEvent {
    String NAME = "CHANNEL_UPDATE";

    Channel getChannel();
}

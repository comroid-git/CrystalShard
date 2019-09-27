package de.kaleidox.crystalshard.core.api.gateway.event.channel;

// https://discordapp.com/developers/docs/topics/gateway#channel-update

import de.kaleidox.crystalshard.api.entity.channel.Channel;
import de.kaleidox.crystalshard.core.api.gateway.event.GatewayEvent;
import de.kaleidox.crystalshard.core.api.gateway.listener.channel.ChannelUpdateListener;
import de.kaleidox.crystalshard.util.annotation.ManagedBy;

@ManagedBy(ChannelUpdateListener.Manager.class)
public interface ChannelUpdateEvent extends GatewayEvent {
    String NAME = "CHANNEL_UPDATE";

    Channel getChannel();
}

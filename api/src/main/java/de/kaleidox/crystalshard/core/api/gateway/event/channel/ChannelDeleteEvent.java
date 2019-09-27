package de.kaleidox.crystalshard.core.api.gateway.event.channel;

// https://discordapp.com/developers/docs/topics/gateway#channel-delete

import de.kaleidox.crystalshard.api.entity.channel.Channel;
import de.kaleidox.crystalshard.core.api.gateway.event.GatewayEvent;
import de.kaleidox.crystalshard.core.api.gateway.listener.channel.ChannelDeleteListener;
import de.kaleidox.crystalshard.util.annotation.ManagedBy;

@ManagedBy(ChannelDeleteListener.Manager.class)
public interface ChannelDeleteEvent extends GatewayEvent {
    String NAME = "CHANNEL_DELETE";

    Channel getChannel();
}

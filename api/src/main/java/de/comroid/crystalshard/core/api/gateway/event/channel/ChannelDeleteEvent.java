package de.comroid.crystalshard.core.api.gateway.event.channel;

// https://discordapp.com/developers/docs/topics/gateway#channel-delete

import de.comroid.crystalshard.api.entity.channel.Channel;
import de.comroid.crystalshard.core.api.gateway.event.GatewayEvent;
import de.comroid.crystalshard.core.api.gateway.listener.channel.ChannelDeleteListener;
import de.comroid.crystalshard.util.annotation.InitializedBy;

public interface ChannelDeleteEvent extends GatewayEvent {
    String NAME = "CHANNEL_DELETE";

    Channel getChannel();
}

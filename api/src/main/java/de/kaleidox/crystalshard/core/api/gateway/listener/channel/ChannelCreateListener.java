package de.kaleidox.crystalshard.core.api.gateway.listener.channel;

import de.kaleidox.crystalshard.core.api.gateway.event.channel.ChannelCreateEvent;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListener;

@FunctionalInterface
public interface ChannelCreateListener extends GatewayListener<ChannelCreateEvent> {
    void onChannelCreate(ChannelCreateEvent event);
}

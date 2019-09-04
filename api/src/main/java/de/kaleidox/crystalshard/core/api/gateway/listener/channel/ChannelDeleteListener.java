package de.kaleidox.crystalshard.core.api.gateway.listener.channel;

import de.kaleidox.crystalshard.core.api.gateway.event.channel.ChannelDeleteEvent;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListener;

@FunctionalInterface
public interface ChannelDeleteListener extends GatewayListener<ChannelDeleteEvent> {
    void onChannelDelete(ChannelDeleteEvent event);
}

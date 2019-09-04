package de.kaleidox.crystalshard.core.api.gateway.listener.channel;

import de.kaleidox.crystalshard.core.api.gateway.event.channel.ChannelUpdateEvent;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListener;

@FunctionalInterface
public interface ChannelUpdateListener extends GatewayListener<ChannelUpdateEvent> {
    void onChannelUpdate(ChannelUpdateEvent event);
}

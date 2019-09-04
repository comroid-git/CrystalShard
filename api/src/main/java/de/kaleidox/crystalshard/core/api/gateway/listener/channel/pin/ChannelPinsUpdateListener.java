package de.kaleidox.crystalshard.core.api.gateway.listener.channel.pin;

import de.kaleidox.crystalshard.core.api.gateway.event.channel.pin.ChannelPinsUpdateEvent;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListener;

@FunctionalInterface
public interface ChannelPinsUpdateListener extends GatewayListener<ChannelPinsUpdateEvent> {
    void onChannelPinsUpdate(ChannelPinsUpdateEvent event);
}

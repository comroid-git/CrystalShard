package de.kaleidox.crystalshard.core.api.gateway.listener.channel.pin;

import de.kaleidox.crystalshard.core.api.gateway.event.channel.pin.ChannelPinsUpdateEvent;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListener;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListenerManager;

public interface ChannelPinsUpdateListener extends GatewayListener<ChannelPinsUpdateEvent> {
    interface Manager extends GatewayListenerManager<ChannelPinsUpdateListener> {
    }
}

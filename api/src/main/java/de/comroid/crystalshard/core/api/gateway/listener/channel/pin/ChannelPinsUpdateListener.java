package de.comroid.crystalshard.core.api.gateway.listener.channel.pin;

import de.comroid.crystalshard.core.api.gateway.event.channel.pin.ChannelPinsUpdateEvent;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListenerManager;

public interface ChannelPinsUpdateListener extends GatewayListener<ChannelPinsUpdateEvent> {
    interface Manager extends GatewayListenerManager<ChannelPinsUpdateListener> {
    }
}

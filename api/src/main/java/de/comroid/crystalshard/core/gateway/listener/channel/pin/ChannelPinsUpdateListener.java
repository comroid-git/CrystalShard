package de.comroid.crystalshard.core.gateway.listener.channel.pin;

import de.comroid.crystalshard.core.gateway.event.channel.pin.CHANNEL_PINS_UPDATE;
import de.comroid.crystalshard.core.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.gateway.listener.GatewayListenerManager;

public interface ChannelPinsUpdateListener extends GatewayListener<CHANNEL_PINS_UPDATE> {
    interface Manager extends GatewayListenerManager<ChannelPinsUpdateListener> {
    }
}

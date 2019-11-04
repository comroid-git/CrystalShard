package de.comroid.crystalshard.core.api.gateway.listener.channel.pin;

import de.comroid.crystalshard.core.api.gateway.event.channel.pin.CHANNEL_PINS_UPDATE;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListenerManager;

public interface ChannelPinsUpdateListener extends GatewayListener<CHANNEL_PINS_UPDATE> {
    interface Manager extends GatewayListenerManager<ChannelPinsUpdateListener> {
    }
}

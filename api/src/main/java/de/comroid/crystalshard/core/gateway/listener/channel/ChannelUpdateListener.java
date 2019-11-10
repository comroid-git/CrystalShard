package de.comroid.crystalshard.core.gateway.listener.channel;

import de.comroid.crystalshard.core.gateway.event.CHANNEL_UPDATE;
import de.comroid.crystalshard.core.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.gateway.listener.GatewayListenerManager;

public interface ChannelUpdateListener extends GatewayListener<CHANNEL_UPDATE> {
    interface Manager extends GatewayListenerManager<ChannelCreateListener> {
    }
}

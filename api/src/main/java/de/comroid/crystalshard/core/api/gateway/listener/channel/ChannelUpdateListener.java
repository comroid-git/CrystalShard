package de.comroid.crystalshard.core.api.gateway.listener.channel;

import de.comroid.crystalshard.core.api.gateway.event.CHANNEL_UPDATE;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListenerManager;

public interface ChannelUpdateListener extends GatewayListener<CHANNEL_UPDATE> {
    interface Manager extends GatewayListenerManager<ChannelCreateListener> {
    }
}

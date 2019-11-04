package de.comroid.crystalshard.core.api.gateway.listener.channel;

import de.comroid.crystalshard.core.api.gateway.event.CHANNEL_CREATE;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListenerManager;

public interface ChannelCreateListener extends GatewayListener<CHANNEL_CREATE> {
    interface Manager extends GatewayListenerManager<ChannelCreateListener> {
    }
}

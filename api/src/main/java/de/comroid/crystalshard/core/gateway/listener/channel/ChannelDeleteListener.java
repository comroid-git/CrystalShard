package de.comroid.crystalshard.core.gateway.listener.channel;

import de.comroid.crystalshard.core.gateway.event.CHANNEL_DELETE;
import de.comroid.crystalshard.core.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.gateway.listener.GatewayListenerManager;

public interface ChannelDeleteListener extends GatewayListener<CHANNEL_DELETE> {
    interface Manager extends GatewayListenerManager<ChannelDeleteListener> {
    }
}

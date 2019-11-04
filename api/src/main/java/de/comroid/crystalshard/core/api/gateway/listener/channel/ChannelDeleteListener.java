package de.comroid.crystalshard.core.api.gateway.listener.channel;

import de.comroid.crystalshard.core.api.gateway.event.CHANNEL_DELETE;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListenerManager;

public interface ChannelDeleteListener extends GatewayListener<CHANNEL_DELETE> {
    interface Manager extends GatewayListenerManager<ChannelDeleteListener> {
    }
}

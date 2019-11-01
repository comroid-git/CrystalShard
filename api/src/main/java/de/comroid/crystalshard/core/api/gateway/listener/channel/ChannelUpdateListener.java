package de.comroid.crystalshard.core.api.gateway.listener.channel;

import de.comroid.crystalshard.core.api.gateway.event.channel.ChannelUpdateEvent;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListenerManager;

public interface ChannelUpdateListener extends GatewayListener<ChannelUpdateEvent> {
    interface Manager extends GatewayListenerManager<ChannelCreateListener> {
    }
}

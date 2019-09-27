package de.kaleidox.crystalshard.core.api.gateway.listener.channel;

import de.kaleidox.crystalshard.core.api.gateway.event.channel.ChannelUpdateEvent;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListener;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListenerManager;

public interface ChannelUpdateListener extends GatewayListener<ChannelUpdateEvent> {
    interface Manager extends GatewayListenerManager<ChannelCreateListener> {
    }
}

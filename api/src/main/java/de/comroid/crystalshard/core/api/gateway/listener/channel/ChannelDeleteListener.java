package de.comroid.crystalshard.core.api.gateway.listener.channel;

import de.comroid.crystalshard.core.api.gateway.event.channel.ChannelDeleteEvent;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListenerManager;

public interface ChannelDeleteListener extends GatewayListener<ChannelDeleteEvent> {
    interface Manager extends GatewayListenerManager<ChannelDeleteListener> {
    }
}

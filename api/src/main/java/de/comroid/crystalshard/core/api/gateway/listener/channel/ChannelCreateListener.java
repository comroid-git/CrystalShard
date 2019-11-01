package de.comroid.crystalshard.core.api.gateway.listener.channel;

import de.comroid.crystalshard.core.api.gateway.event.channel.ChannelCreateEvent;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListenerManager;

public interface ChannelCreateListener extends GatewayListener<ChannelCreateEvent> {
    interface Manager extends GatewayListenerManager<ChannelCreateListener> {
    }
}

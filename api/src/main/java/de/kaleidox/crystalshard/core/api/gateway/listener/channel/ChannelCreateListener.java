package de.kaleidox.crystalshard.core.api.gateway.listener.channel;

import de.kaleidox.crystalshard.core.api.gateway.event.channel.ChannelCreateEvent;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListener;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListenerManager;

public interface ChannelCreateListener extends GatewayListener<ChannelCreateEvent> {
    interface Manager extends GatewayListenerManager<ChannelCreateListener> {
    }
}

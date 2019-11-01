package de.comroid.crystalshard.core.api.gateway.listener.channel.type;

import de.comroid.crystalshard.core.api.gateway.event.channel.type.ChannelTypingStartEvent;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListenerManager;

public interface TypingStartListener extends GatewayListener<ChannelTypingStartEvent> {
    interface Manager extends GatewayListenerManager<TypingStartListener> {
    }
}

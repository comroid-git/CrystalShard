package de.comroid.crystalshard.core.gateway.listener.channel.type;

import de.comroid.crystalshard.core.gateway.event.TYPING_START;
import de.comroid.crystalshard.core.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.gateway.listener.GatewayListenerManager;

public interface TypingStartListener extends GatewayListener<TYPING_START> {
    interface Manager extends GatewayListenerManager<TypingStartListener> {
    }
}

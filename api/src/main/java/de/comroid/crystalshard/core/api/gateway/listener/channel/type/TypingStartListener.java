package de.comroid.crystalshard.core.api.gateway.listener.channel.type;

import de.comroid.crystalshard.core.api.gateway.event.TYPING_START;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListenerManager;

public interface TypingStartListener extends GatewayListener<TYPING_START> {
    interface Manager extends GatewayListenerManager<TypingStartListener> {
    }
}

package de.comroid.crystalshard.core.gateway.listener.voice;

import de.comroid.crystalshard.core.gateway.event.VoiceServerUpdateEvent;
import de.comroid.crystalshard.core.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.gateway.listener.GatewayListenerManager;

public interface VoiceServerUpdateListener extends GatewayListener<VoiceServerUpdateEvent> {
    interface Manager extends GatewayListenerManager<VoiceServerUpdateListener> {
    }
}

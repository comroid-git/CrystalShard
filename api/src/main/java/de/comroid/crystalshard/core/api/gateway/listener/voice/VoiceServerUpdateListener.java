package de.comroid.crystalshard.core.api.gateway.listener.voice;

import de.comroid.crystalshard.core.api.gateway.event.VoiceServerUpdateEvent;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListenerManager;

public interface VoiceServerUpdateListener extends GatewayListener<VoiceServerUpdateEvent> {
    interface Manager extends GatewayListenerManager<VoiceServerUpdateListener> {
    }
}

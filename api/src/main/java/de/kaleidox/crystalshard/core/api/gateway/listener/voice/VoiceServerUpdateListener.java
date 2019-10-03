package de.kaleidox.crystalshard.core.api.gateway.listener.voice;

import de.kaleidox.crystalshard.core.api.gateway.event.voice.VoiceServerUpdateEvent;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListener;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListenerManager;

public interface VoiceServerUpdateListener extends GatewayListener<VoiceServerUpdateEvent> {
    interface Manager extends GatewayListenerManager<VoiceServerUpdateListener> {
    }
}
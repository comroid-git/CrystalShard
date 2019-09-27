package de.kaleidox.crystalshard.core.api.gateway.listener.voice;

import de.kaleidox.crystalshard.core.api.gateway.event.voice.VoiceStateUpdateEvent;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListener;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListenerManager;

public interface VoiceStateUpdateListener extends GatewayListener<VoiceStateUpdateEvent> {
    interface Manager extends GatewayListenerManager<VoiceStateUpdateListener> {
    }
}

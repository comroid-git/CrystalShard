package de.kaleidox.crystalshard.core.api.gateway.listener.voice;

import de.kaleidox.crystalshard.core.api.gateway.event.voice.VoiceStateUpdateEvent;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListener;

@FunctionalInterface
public interface VoiceStateUpdateListener extends GatewayListener<VoiceStateUpdateEvent> {
    void onVoiceStateUpdate(VoiceStateUpdateEvent event);
}

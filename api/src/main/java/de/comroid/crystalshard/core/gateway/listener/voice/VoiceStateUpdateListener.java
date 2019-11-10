package de.comroid.crystalshard.core.gateway.listener.voice;

import de.comroid.crystalshard.core.gateway.event.VOICE_STATE_UPDATE;
import de.comroid.crystalshard.core.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.gateway.listener.GatewayListenerManager;

public interface VoiceStateUpdateListener extends GatewayListener<VOICE_STATE_UPDATE> {
    interface Manager extends GatewayListenerManager<VoiceStateUpdateListener> {
    }
}

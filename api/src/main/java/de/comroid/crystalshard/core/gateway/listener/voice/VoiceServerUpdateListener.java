package de.comroid.crystalshard.core.gateway.listener.voice;

import de.comroid.crystalshard.core.gateway.event.VOICE_SERVER_UPDATE;
import de.comroid.crystalshard.core.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.gateway.listener.GatewayListenerManager;

public interface VoiceServerUpdateListener extends GatewayListener<VOICE_SERVER_UPDATE> {
    interface Manager extends GatewayListenerManager<VoiceServerUpdateListener> {
    }
}

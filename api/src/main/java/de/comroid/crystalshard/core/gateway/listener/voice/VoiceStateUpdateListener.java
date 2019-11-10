package de.comroid.crystalshard.core.gateway.listener.voice;

import de.comroid.crystalshard.core.gateway.event.VoiceStateUpdateEvent;
import de.comroid.crystalshard.core.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.gateway.listener.GatewayListenerManager;

public interface VoiceStateUpdateListener extends GatewayListener<VoiceStateUpdateEvent> {
    interface Manager extends GatewayListenerManager<VoiceStateUpdateListener> {
    }
}

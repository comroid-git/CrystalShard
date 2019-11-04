package de.comroid.crystalshard.core.api.gateway.listener.voice;

import de.comroid.crystalshard.core.api.gateway.event.VoiceStateUpdateEvent;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListenerManager;

public interface VoiceStateUpdateListener extends GatewayListener<VoiceStateUpdateEvent> {
    interface Manager extends GatewayListenerManager<VoiceStateUpdateListener> {
    }
}

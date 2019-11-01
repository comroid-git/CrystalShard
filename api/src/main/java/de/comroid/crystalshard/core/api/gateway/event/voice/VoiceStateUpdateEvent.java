package de.comroid.crystalshard.core.api.gateway.event.voice;

// https://discordapp.com/developers/docs/topics/gateway#voice-state-update

import de.comroid.crystalshard.api.model.voice.VoiceState;
import de.comroid.crystalshard.core.api.gateway.event.GatewayEvent;
import de.comroid.crystalshard.core.api.gateway.listener.voice.VoiceStateUpdateListener;
import de.comroid.crystalshard.util.annotation.ManagedBy;

@ManagedBy(VoiceStateUpdateListener.Manager.class)
public interface VoiceStateUpdateEvent extends GatewayEvent {
    String NAME = "VOICE_STATE_UPDATE";

    VoiceState getVoiceState();
}

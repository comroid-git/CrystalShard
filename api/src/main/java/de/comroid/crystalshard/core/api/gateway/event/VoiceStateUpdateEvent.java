package de.comroid.crystalshard.core.api.gateway.event;

// https://discordapp.com/developers/docs/topics/gateway#voice-state-update

import de.comroid.crystalshard.api.model.voice.VoiceState;

public interface VoiceStateUpdateEvent extends GatewayEventBase {
    String NAME = "VOICE_STATE_UPDATE";

    VoiceState getVoiceState();
}

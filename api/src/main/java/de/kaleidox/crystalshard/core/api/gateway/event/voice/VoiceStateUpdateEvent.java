package de.kaleidox.crystalshard.core.api.gateway.event.voice;

// https://discordapp.com/developers/docs/topics/gateway#voice-state-update

import java.net.URL;

import de.kaleidox.crystalshard.api.entity.guild.Guild;
import de.kaleidox.crystalshard.core.api.gateway.event.GatewayEvent;

public interface VoiceStateUpdateEvent extends GatewayEvent {
    String NAME = "VOICE_STATE_UPDATE";

    String getVoiceConnectionToken();

    Guild getGuild();

    URL getEndpoint();
}

package de.kaleidox.crystalshard.core.api.gateway.event.voice;

import java.net.URL;

import de.kaleidox.crystalshard.api.entity.guild.Guild;
import de.kaleidox.crystalshard.core.api.gateway.event.GatewayEvent;
import de.kaleidox.crystalshard.core.api.gateway.listener.voice.VoiceServerUpdateListener;
import de.kaleidox.crystalshard.util.annotation.ManagedBy;

@ManagedBy(VoiceServerUpdateListener.Manager.class)
public interface VoiceServerUpdateEvent extends GatewayEvent {
    String NAME = "VOICE_SERVER_UPDATE";

    String getToken();

    Guild getGuild();

    URL getEndpointURL();
}

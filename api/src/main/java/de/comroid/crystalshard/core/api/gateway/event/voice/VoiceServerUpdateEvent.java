package de.comroid.crystalshard.core.api.gateway.event.voice;

import java.net.URL;

import de.comroid.crystalshard.api.entity.guild.Guild;
import de.comroid.crystalshard.core.api.gateway.event.GatewayEvent;
import de.comroid.crystalshard.core.api.gateway.listener.voice.VoiceServerUpdateListener;
import de.comroid.crystalshard.util.annotation.InitializedBy;

public interface VoiceServerUpdateEvent extends GatewayEvent {
    String NAME = "VOICE_SERVER_UPDATE";

    String getToken();

    Guild getGuild();

    URL getEndpointURL();
}

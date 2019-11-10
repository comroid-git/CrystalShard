package de.comroid.crystalshard.core.gateway.event;

import java.net.URL;

import de.comroid.crystalshard.api.entity.guild.Guild;

public interface VoiceServerUpdateEvent extends GatewayEventBase {
    String NAME = "VOICE_SERVER_UPDATE";

    String getToken();

    Guild getGuild();

    URL getEndpointURL();
}

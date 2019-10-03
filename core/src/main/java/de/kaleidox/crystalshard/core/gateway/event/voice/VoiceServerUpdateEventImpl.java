package de.kaleidox.crystalshard.core.gateway.event.voice;

import java.net.MalformedURLException;
import java.net.URL;

import de.kaleidox.crystalshard.abstraction.gateway.AbstractGatewayEvent;
import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.api.entity.guild.Guild;
import de.kaleidox.crystalshard.core.annotation.JsonData;
import de.kaleidox.crystalshard.core.api.gateway.event.voice.VoiceServerUpdateEvent;
import de.kaleidox.crystalshard.util.annotation.IntroducedBy;

import com.fasterxml.jackson.databind.JsonNode;

import static de.kaleidox.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;

@IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/topics/gateway#voice-server-update")
public class VoiceServerUpdateEventImpl extends AbstractGatewayEvent implements VoiceServerUpdateEvent {
    protected @JsonData("token") String token;
    protected @JsonData("guild_id") long guildId;
    protected @JsonData("endpoint") String endpointStr;

    private Guild guild;
    private URL endpointUrl;

    public VoiceServerUpdateEventImpl(Discord api, JsonNode data) {
        super(api, data);

        guild = api.getCacheManager()
                .getGuildByID(guildId)
                .orElseThrow(() -> new AssertionError("No valid Guild ID was sent with this VoiceServerUpdateEvent!"));
        try {
            endpointUrl = new URL(endpointStr);
        } catch (MalformedURLException e) {
            throw new AssertionError("Unexpected MalformedURLException", e);
        }

        affects(guild);
        // todo affects guild.getVoiceConnections
    }

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public Guild getGuild() {
        return guild;
    }

    @Override
    public URL getEndpointURL() {
        return endpointUrl;
    }
}

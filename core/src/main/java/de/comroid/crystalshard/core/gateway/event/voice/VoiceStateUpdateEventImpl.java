package de.comroid.crystalshard.core.gateway.event.voice;

import de.comroid.crystalshard.abstraction.gateway.AbstractGatewayEvent;
import de.comroid.crystalshard.api.Discord;
import de.comroid.crystalshard.api.model.voice.VoiceState;
import de.comroid.crystalshard.core.annotation.JsonData;
import de.comroid.crystalshard.core.api.gateway.event.voice.VoiceStateUpdateEvent;
import de.comroid.crystalshard.util.annotation.IntroducedBy;

import com.fasterxml.jackson.databind.JsonNode;

import static de.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;

@IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/topics/gateway#voice-state-update")
public class VoiceStateUpdateEventImpl extends AbstractGatewayEvent implements VoiceStateUpdateEvent {
    protected @JsonData VoiceState voiceState;

    public VoiceStateUpdateEventImpl(Discord api, JsonNode data) {
        super(api, data);

        affects(voiceState.getUser());
        voiceState.getChannel().ifPresent(this::affects);
        voiceState.getGuildMember().ifPresent(member -> {
            affects(member);
            affects(member.getGuild());

            member.getRoles().forEach(this::affects);
        });
    }

    @Override
    public VoiceState getVoiceState() {
        return voiceState;
    }
}

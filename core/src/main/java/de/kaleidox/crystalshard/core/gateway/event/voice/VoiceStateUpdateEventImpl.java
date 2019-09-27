package de.kaleidox.crystalshard.core.gateway.event.voice;

import de.kaleidox.crystalshard.abstraction.gateway.AbstractGatewayEvent;
import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.api.model.voice.VoiceState;
import de.kaleidox.crystalshard.core.annotation.JsonData;
import de.kaleidox.crystalshard.core.api.gateway.event.voice.VoiceStateUpdateEvent;
import de.kaleidox.crystalshard.util.annotation.IntroducedBy;

import com.fasterxml.jackson.databind.JsonNode;

import static de.kaleidox.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;

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

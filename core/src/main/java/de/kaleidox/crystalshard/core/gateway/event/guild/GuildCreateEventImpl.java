package de.kaleidox.crystalshard.core.gateway.event.guild;

import de.kaleidox.crystalshard.abstraction.gateway.AbstractGatewayEvent;
import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.api.entity.guild.Guild;
import de.kaleidox.crystalshard.api.listener.model.ListenerAttachable;
import de.kaleidox.crystalshard.core.annotation.JsonData;
import de.kaleidox.crystalshard.core.api.gateway.event.guild.GuildCreateEvent;
import de.kaleidox.crystalshard.util.annotation.IntroducedBy;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import static de.kaleidox.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;

@IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/topics/gateway#guild-create")
public class GuildCreateEventImpl extends AbstractGatewayEvent implements GuildCreateEvent {
    private @JsonData Guild createdGuild;

    public GuildCreateEventImpl(Discord api, JsonNode data) {
        super(api, data);
    }

    @Override
    public Guild getGuild() {
        return createdGuild;
    }
}

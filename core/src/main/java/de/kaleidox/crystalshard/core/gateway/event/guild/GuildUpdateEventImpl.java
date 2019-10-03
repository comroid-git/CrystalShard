package de.kaleidox.crystalshard.core.gateway.event.guild;

import de.kaleidox.crystalshard.abstraction.gateway.AbstractGatewayEvent;
import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.api.entity.guild.Guild;
import de.kaleidox.crystalshard.core.annotation.JsonData;
import de.kaleidox.crystalshard.core.api.gateway.event.guild.GuildUpdateEvent;
import de.kaleidox.crystalshard.util.annotation.IntroducedBy;

import com.fasterxml.jackson.databind.JsonNode;

import static de.kaleidox.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;

@IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/topics/gateway#guild-update")
public class GuildUpdateEventImpl extends AbstractGatewayEvent implements GuildUpdateEvent {
    protected @JsonData Guild updatedGuild;

    public GuildUpdateEventImpl(Discord api, JsonNode data) {
        super(api, data);

        affects(updatedGuild);
    }

    @Override
    public Guild getGuild() {
        return updatedGuild;
    }
}

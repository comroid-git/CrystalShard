package de.comroid.crystalshard.core.gateway.event.guild;

import de.comroid.crystalshard.abstraction.gateway.AbstractGatewayEvent;
import de.comroid.crystalshard.api.Discord;
import de.comroid.crystalshard.api.entity.guild.Guild;
import de.comroid.crystalshard.core.annotation.JsonData;
import de.comroid.crystalshard.core.api.gateway.event.guild.GuildUpdateEvent;
import de.comroid.crystalshard.util.annotation.IntroducedBy;

import com.fasterxml.jackson.databind.JsonNode;

import static de.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;

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

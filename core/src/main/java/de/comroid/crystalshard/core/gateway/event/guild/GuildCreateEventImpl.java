package de.comroid.crystalshard.core.gateway.event.guild;

import de.comroid.crystalshard.abstraction.gateway.AbstractGatewayEvent;
import de.comroid.crystalshard.api.Discord;
import de.comroid.crystalshard.api.entity.guild.Guild;
import de.comroid.crystalshard.core.annotation.JsonData;
import de.comroid.crystalshard.core.api.gateway.event.guild.GuildCreateEvent;
import de.comroid.crystalshard.util.annotation.IntroducedBy;

import com.fasterxml.jackson.databind.JsonNode;

import static de.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;

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

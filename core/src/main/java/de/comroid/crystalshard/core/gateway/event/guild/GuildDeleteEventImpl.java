package de.comroid.crystalshard.core.gateway.event.guild;

import de.comroid.crystalshard.abstraction.gateway.AbstractGatewayEvent;
import de.comroid.crystalshard.api.Discord;
import de.comroid.crystalshard.api.entity.Snowflake;
import de.comroid.crystalshard.api.entity.guild.Guild;
import de.comroid.crystalshard.core.annotation.JsonData;
import de.comroid.crystalshard.core.api.gateway.event.guild.GuildDeleteEvent;
import de.comroid.crystalshard.util.annotation.IntroducedBy;

import com.fasterxml.jackson.databind.JsonNode;

import static de.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;

@IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/topics/gateway#guild-delete")
public class GuildDeleteEventImpl extends AbstractGatewayEvent implements GuildDeleteEvent {
    protected @JsonData Guild unavailableGuild;

    private boolean isUnavailable;

    public GuildDeleteEventImpl(Discord api, JsonNode data) {
        super(api, data);

        isUnavailable = data.has("unavailable");

        affects(unavailableGuild);
    }

    @Override
    public Snowflake getGuildID() {
        return unavailableGuild;
    }

    @Override
    public boolean isUnavailable() {
        return isUnavailable;
    }

    @Override
    public boolean isRemoved() {
        return !isUnavailable;
    }
}

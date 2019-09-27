package de.kaleidox.crystalshard.core.gateway.event.guild;

import de.kaleidox.crystalshard.abstraction.gateway.AbstractGatewayEvent;
import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.api.entity.Snowflake;
import de.kaleidox.crystalshard.api.entity.guild.Guild;
import de.kaleidox.crystalshard.api.listener.model.ListenerAttachable;
import de.kaleidox.crystalshard.core.annotation.JsonData;
import de.kaleidox.crystalshard.core.api.gateway.event.guild.GuildDeleteEvent;
import de.kaleidox.crystalshard.util.annotation.IntroducedBy;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import static de.kaleidox.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;

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

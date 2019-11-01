package de.comroid.crystalshard.core.gateway.event.guild.integration;

import de.comroid.crystalshard.abstraction.gateway.AbstractGatewayEvent;
import de.comroid.crystalshard.api.Discord;
import de.comroid.crystalshard.api.entity.guild.Guild;
import de.comroid.crystalshard.core.annotation.JsonData;
import de.comroid.crystalshard.core.api.gateway.event.guild.integration.GuildIntegrationsUpdateEvent;
import de.comroid.crystalshard.util.annotation.IntroducedBy;

import com.fasterxml.jackson.databind.JsonNode;

import static de.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;

@IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/topics/gateway#guild-integrations-update")
public class GuildIntegrationsUpdateEventImpl extends AbstractGatewayEvent implements GuildIntegrationsUpdateEvent {
    protected @JsonData("guild_id") long guildId;

    private Guild guild;

    public GuildIntegrationsUpdateEventImpl(Discord api, JsonNode data) {
        super(api, data);

        guild = api.getCacheManager()
                .getGuildByID(guildId)
                .orElseThrow(() -> new AssertionError("No Guild ID was sent with this GuildIntegrationsUpdateEvent!"));

        affects(guild);
    }

    @Override
    public Guild getGuild() {
        return guild;
    }
}

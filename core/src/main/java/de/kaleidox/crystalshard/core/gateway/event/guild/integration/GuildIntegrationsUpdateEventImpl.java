package de.kaleidox.crystalshard.core.gateway.event.guild.integration;

import de.kaleidox.crystalshard.abstraction.gateway.AbstractGatewayEvent;
import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.api.entity.guild.Guild;
import de.kaleidox.crystalshard.api.listener.model.ListenerAttachable;
import de.kaleidox.crystalshard.core.annotation.JsonData;
import de.kaleidox.crystalshard.core.api.gateway.event.guild.integration.GuildIntegrationsUpdateEvent;
import de.kaleidox.crystalshard.util.annotation.IntroducedBy;

import com.fasterxml.jackson.databind.JsonNode;

import static de.kaleidox.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;

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

package de.kaleidox.crystalshard.core.gateway.event.guild.role;

import de.kaleidox.crystalshard.abstraction.gateway.AbstractGatewayEvent;
import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.api.entity.guild.Guild;
import de.kaleidox.crystalshard.api.entity.guild.Role;
import de.kaleidox.crystalshard.core.annotation.JsonData;
import de.kaleidox.crystalshard.core.api.gateway.event.guild.role.GuildRoleCreateEvent;
import de.kaleidox.crystalshard.util.annotation.IntroducedBy;

import com.fasterxml.jackson.databind.JsonNode;

import static de.kaleidox.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;

@IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/topics/gateway#guild-role-create")
public class GuildRoleCreateEventImpl extends AbstractGatewayEvent implements GuildRoleCreateEvent {
    protected @JsonData("guild_id") long guildId;
    protected @JsonData("role") Role role;

    private Guild guild;

    public GuildRoleCreateEventImpl(Discord api, JsonNode data) {
        super(api, data);

        guild = api.getCacheManager()
                .getGuildByID(guildId)
                .orElseThrow(() -> new AssertionError("No Guild ID was sent with this GuildRoleCreateEvent!"));

        affects(guild);
    }

    @Override
    public Guild getGuild() {
        return guild;
    }

    @Override
    public Role getRole() {
        return role;
    }
}

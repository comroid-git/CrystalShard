package de.kaleidox.crystalshard.core.gateway.event.guild.role;

import de.kaleidox.crystalshard.abstraction.gateway.AbstractGatewayEvent;
import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.api.entity.guild.Guild;
import de.kaleidox.crystalshard.api.entity.guild.Role;
import de.kaleidox.crystalshard.core.annotation.JsonData;
import de.kaleidox.crystalshard.core.api.gateway.event.guild.role.GuildRoleDeleteEvent;
import de.kaleidox.crystalshard.util.annotation.IntroducedBy;

import com.fasterxml.jackson.databind.JsonNode;

import static de.kaleidox.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;

@IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/topics/gateway#guild-role-delete")
public class GuildRoleDeleteEventImpl extends AbstractGatewayEvent implements GuildRoleDeleteEvent {
    protected @JsonData("guild_id") long guildId;
    protected @JsonData("role_id") long roleId;

    private Guild guild;
    private Role role;

    public GuildRoleDeleteEventImpl(Discord api, JsonNode data) {
        super(api, data);

        guild = api.getCacheManager()
                .getGuildByID(guildId)
                .orElseThrow(() -> new AssertionError("No Guild ID was sent with this GuildRoleDeleteEvent!"));
        role = api.getCacheManager()
                .getRoleByID(guildId, roleId)
                .orElseThrow(() -> new AssertionError("No Role ID was sent with this GuildRoleDeleteEvent!"));

        affects(guild);
        affects(role);
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

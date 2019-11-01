package de.comroid.crystalshard.core.api.gateway.event.guild.role;

// https://discordapp.com/developers/docs/topics/gateway#guild-role-create

import de.comroid.crystalshard.api.entity.guild.Guild;
import de.comroid.crystalshard.api.entity.guild.Role;
import de.comroid.crystalshard.core.api.gateway.event.GatewayEvent;
import de.comroid.crystalshard.core.api.gateway.listener.guild.role.GuildRoleCreateListener;
import de.comroid.crystalshard.util.annotation.ManagedBy;

@ManagedBy(GuildRoleCreateListener.Manager.class)
public interface GuildRoleCreateEvent extends GatewayEvent {
    String NAME = "GUILD_ROLE_CREATE";

    Guild getGuild();

    Role getRole();
}

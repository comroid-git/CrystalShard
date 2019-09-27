package de.kaleidox.crystalshard.core.api.gateway.event.guild.role;

// https://discordapp.com/developers/docs/topics/gateway#guild-role-delete

import de.kaleidox.crystalshard.api.entity.guild.Guild;
import de.kaleidox.crystalshard.api.entity.guild.Role;
import de.kaleidox.crystalshard.core.api.gateway.event.GatewayEvent;
import de.kaleidox.crystalshard.core.api.gateway.listener.guild.role.GuildRoleDeleteListener;
import de.kaleidox.crystalshard.util.annotation.ManagedBy;

@ManagedBy(GuildRoleDeleteListener.Manager.class)
public interface GuildRoleDeleteEvent extends GatewayEvent {
    String NAME = "GUILD_ROLE_DELETE";

    Guild getGuild();

    Role getRole();
}

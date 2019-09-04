package de.kaleidox.crystalshard.core.api.gateway.event.guild.role;

// https://discordapp.com/developers/docs/topics/gateway#guild-role-update

import de.kaleidox.crystalshard.api.entity.guild.Guild;
import de.kaleidox.crystalshard.api.entity.guild.Role;
import de.kaleidox.crystalshard.core.api.gateway.event.GatewayEvent;

public interface GuildRoleUpdateEvent extends GatewayEvent {
    String NAME = "GUILD_ROLE_UPDATE";

    Guild getGuild();

    Role getRole();
}

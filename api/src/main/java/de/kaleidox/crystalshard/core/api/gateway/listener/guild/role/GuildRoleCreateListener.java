package de.kaleidox.crystalshard.core.api.gateway.listener.guild.role;

import de.kaleidox.crystalshard.core.api.gateway.event.guild.role.GuildRoleCreateEvent;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListener;

@FunctionalInterface
public interface GuildRoleCreateListener extends GatewayListener<GuildRoleCreateEvent> {
    void onGuildRoleCreate(GuildRoleCreateEvent event);
}

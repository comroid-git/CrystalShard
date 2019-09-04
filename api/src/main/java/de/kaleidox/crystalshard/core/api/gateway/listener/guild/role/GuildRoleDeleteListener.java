package de.kaleidox.crystalshard.core.api.gateway.listener.guild.role;

import de.kaleidox.crystalshard.core.api.gateway.event.guild.role.GuildRoleDeleteEvent;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListener;

@FunctionalInterface
public interface GuildRoleDeleteListener extends GatewayListener<GuildRoleDeleteEvent> {
    void onGuildRoleDelete(GuildRoleDeleteEvent event);
}

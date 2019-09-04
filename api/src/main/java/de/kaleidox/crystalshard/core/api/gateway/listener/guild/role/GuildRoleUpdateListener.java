package de.kaleidox.crystalshard.core.api.gateway.listener.guild.role;

import de.kaleidox.crystalshard.core.api.gateway.event.guild.role.GuildRoleUpdateEvent;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListener;

@FunctionalInterface
public interface GuildRoleUpdateListener extends GatewayListener<GuildRoleUpdateEvent> {
    void onGuildRoleUpdate(GuildRoleUpdateEvent event);
}

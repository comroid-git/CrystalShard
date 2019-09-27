package de.kaleidox.crystalshard.core.api.gateway.listener.guild.role;

import de.kaleidox.crystalshard.core.api.gateway.event.guild.role.GuildRoleUpdateEvent;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListener;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListenerManager;

public interface GuildRoleUpdateListener extends GatewayListener<GuildRoleUpdateEvent> {
    interface Manager extends GatewayListenerManager<GuildRoleUpdateListener> {
    }
}

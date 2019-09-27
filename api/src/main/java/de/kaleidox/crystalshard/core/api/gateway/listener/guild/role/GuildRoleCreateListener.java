package de.kaleidox.crystalshard.core.api.gateway.listener.guild.role;

import de.kaleidox.crystalshard.core.api.gateway.event.guild.role.GuildRoleCreateEvent;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListener;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListenerManager;

public interface GuildRoleCreateListener extends GatewayListener<GuildRoleCreateEvent> {
    interface Manager extends GatewayListenerManager<GuildRoleCreateListener> {
    }
}

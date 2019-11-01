package de.comroid.crystalshard.core.api.gateway.listener.guild.role;

import de.comroid.crystalshard.core.api.gateway.event.guild.role.GuildRoleUpdateEvent;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListenerManager;

public interface GuildRoleUpdateListener extends GatewayListener<GuildRoleUpdateEvent> {
    interface Manager extends GatewayListenerManager<GuildRoleUpdateListener> {
    }
}

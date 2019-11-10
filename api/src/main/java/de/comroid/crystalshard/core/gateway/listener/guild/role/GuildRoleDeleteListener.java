package de.comroid.crystalshard.core.gateway.listener.guild.role;

import de.comroid.crystalshard.core.gateway.event.GuildRoleDeleteEvent;
import de.comroid.crystalshard.core.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.gateway.listener.GatewayListenerManager;

public interface GuildRoleDeleteListener extends GatewayListener<GuildRoleDeleteEvent> {
    interface Manager extends GatewayListenerManager<GuildRoleDeleteListener> {
    }
}

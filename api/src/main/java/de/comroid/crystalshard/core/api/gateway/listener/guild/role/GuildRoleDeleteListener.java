package de.comroid.crystalshard.core.api.gateway.listener.guild.role;

import de.comroid.crystalshard.core.api.gateway.event.GuildRoleDeleteEvent;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListenerManager;

public interface GuildRoleDeleteListener extends GatewayListener<GuildRoleDeleteEvent> {
    interface Manager extends GatewayListenerManager<GuildRoleDeleteListener> {
    }
}

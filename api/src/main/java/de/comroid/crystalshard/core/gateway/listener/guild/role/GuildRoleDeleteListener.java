package de.comroid.crystalshard.core.gateway.listener.guild.role;

import de.comroid.crystalshard.core.gateway.event.GUILD_ROLE_DELETE;
import de.comroid.crystalshard.core.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.gateway.listener.GatewayListenerManager;

public interface GuildRoleDeleteListener extends GatewayListener<GUILD_ROLE_DELETE> {
    interface Manager extends GatewayListenerManager<GuildRoleDeleteListener> {
    }
}

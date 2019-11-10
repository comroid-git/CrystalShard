package de.comroid.crystalshard.core.gateway.listener.guild.role;

import de.comroid.crystalshard.core.gateway.event.GUILD_ROLE_CREATE;
import de.comroid.crystalshard.core.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.gateway.listener.GatewayListenerManager;

public interface GuildRoleCreateListener extends GatewayListener<GUILD_ROLE_CREATE> {
    interface Manager extends GatewayListenerManager<GuildRoleCreateListener> {
    }
}

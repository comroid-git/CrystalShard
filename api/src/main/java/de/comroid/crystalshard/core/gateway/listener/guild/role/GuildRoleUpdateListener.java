package de.comroid.crystalshard.core.gateway.listener.guild.role;

import de.comroid.crystalshard.core.gateway.event.GUILD_ROLE_UPDATE;
import de.comroid.crystalshard.core.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.gateway.listener.GatewayListenerManager;

public interface GuildRoleUpdateListener extends GatewayListener<GUILD_ROLE_UPDATE> {
    interface Manager extends GatewayListenerManager<GuildRoleUpdateListener> {
    }
}

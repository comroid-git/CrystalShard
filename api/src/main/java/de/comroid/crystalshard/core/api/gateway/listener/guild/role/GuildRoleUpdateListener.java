package de.comroid.crystalshard.core.api.gateway.listener.guild.role;

import de.comroid.crystalshard.core.api.gateway.event.GUILD_ROLE_UPDATE;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListenerManager;

public interface GuildRoleUpdateListener extends GatewayListener<GUILD_ROLE_UPDATE> {
    interface Manager extends GatewayListenerManager<GuildRoleUpdateListener> {
    }
}

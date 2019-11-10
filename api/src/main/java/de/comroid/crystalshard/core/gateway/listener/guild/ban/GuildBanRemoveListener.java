package de.comroid.crystalshard.core.gateway.listener.guild.ban;

import de.comroid.crystalshard.core.gateway.event.GUILD_BAN_REMOVE;
import de.comroid.crystalshard.core.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.gateway.listener.GatewayListenerManager;

public interface GuildBanRemoveListener extends GatewayListener<GUILD_BAN_REMOVE> {
    interface Manager extends GatewayListenerManager<GuildBanRemoveListener> {
    }
}

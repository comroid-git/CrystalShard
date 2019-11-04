package de.comroid.crystalshard.core.api.gateway.listener.guild.ban;

import de.comroid.crystalshard.core.api.gateway.event.GUILD_BAN_REMOVE;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListenerManager;

public interface GuildBanRemoveListener extends GatewayListener<GUILD_BAN_REMOVE> {
    interface Manager extends GatewayListenerManager<GuildBanRemoveListener> {
    }
}

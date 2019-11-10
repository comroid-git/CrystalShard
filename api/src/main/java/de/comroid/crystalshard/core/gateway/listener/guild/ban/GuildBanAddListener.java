package de.comroid.crystalshard.core.gateway.listener.guild.ban;

import de.comroid.crystalshard.core.gateway.event.GUILD_BAN_ADD;
import de.comroid.crystalshard.core.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.gateway.listener.GatewayListenerManager;

public interface GuildBanAddListener extends GatewayListener<GUILD_BAN_ADD> {
    interface Manager extends GatewayListenerManager<GuildBanAddListener> {
    }
}

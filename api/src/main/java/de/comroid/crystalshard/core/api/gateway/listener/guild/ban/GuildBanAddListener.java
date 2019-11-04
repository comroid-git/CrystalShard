package de.comroid.crystalshard.core.api.gateway.listener.guild.ban;

import de.comroid.crystalshard.core.api.gateway.event.GUILD_BAN_ADD;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListenerManager;

public interface GuildBanAddListener extends GatewayListener<GUILD_BAN_ADD> {
    interface Manager extends GatewayListenerManager<GuildBanAddListener> {
    }
}

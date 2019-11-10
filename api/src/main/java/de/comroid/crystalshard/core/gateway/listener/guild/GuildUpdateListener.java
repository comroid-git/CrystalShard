package de.comroid.crystalshard.core.gateway.listener.guild;

import de.comroid.crystalshard.core.gateway.event.GUILD_UPDATE;
import de.comroid.crystalshard.core.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.gateway.listener.GatewayListenerManager;

public interface GuildUpdateListener extends GatewayListener<GUILD_UPDATE> {
    interface Manager extends GatewayListenerManager<GuildUpdateListener> {
    }
}

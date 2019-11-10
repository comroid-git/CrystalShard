package de.comroid.crystalshard.core.gateway.listener.guild;

import de.comroid.crystalshard.core.gateway.event.GUILD_CREATE;
import de.comroid.crystalshard.core.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.gateway.listener.GatewayListenerManager;

public interface GuildCreateListener extends GatewayListener<GUILD_CREATE> {
    interface Manager extends GatewayListenerManager<GuildCreateListener> {
    }
}

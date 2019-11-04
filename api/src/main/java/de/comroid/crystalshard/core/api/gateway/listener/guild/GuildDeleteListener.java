package de.comroid.crystalshard.core.api.gateway.listener.guild;

import de.comroid.crystalshard.core.api.gateway.event.GUILD_DELETE;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListenerManager;

public interface GuildDeleteListener extends GatewayListener<GUILD_DELETE> {
    interface Manager extends GatewayListenerManager<GuildDeleteListener> {
    }
}

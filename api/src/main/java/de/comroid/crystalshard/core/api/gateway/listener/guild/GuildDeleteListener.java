package de.comroid.crystalshard.core.api.gateway.listener.guild;

import de.comroid.crystalshard.core.api.gateway.event.guild.GuildDeleteEvent;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListenerManager;

public interface GuildDeleteListener extends GatewayListener<GuildDeleteEvent> {
    interface Manager extends GatewayListenerManager<GuildDeleteListener> {
    }
}

package de.kaleidox.crystalshard.core.api.gateway.listener.guild;

import de.kaleidox.crystalshard.core.api.gateway.event.guild.GuildDeleteEvent;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListener;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListenerManager;

public interface GuildDeleteListener extends GatewayListener<GuildDeleteEvent> {
    interface Manager extends GatewayListenerManager<GuildDeleteListener> {
    }
}

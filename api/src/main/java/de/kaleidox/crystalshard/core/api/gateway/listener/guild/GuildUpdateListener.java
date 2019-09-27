package de.kaleidox.crystalshard.core.api.gateway.listener.guild;

import de.kaleidox.crystalshard.core.api.gateway.event.guild.GuildUpdateEvent;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListener;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListenerManager;

public interface GuildUpdateListener extends GatewayListener<GuildUpdateEvent> {
    interface Manager extends GatewayListenerManager<GuildUpdateListener> {
    }
}

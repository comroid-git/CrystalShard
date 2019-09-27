package de.kaleidox.crystalshard.core.api.gateway.listener.guild;

import de.kaleidox.crystalshard.core.api.gateway.event.guild.GuildCreateEvent;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListener;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListenerManager;

public interface GuildCreateListener extends GatewayListener<GuildCreateEvent> {
    interface Manager extends GatewayListenerManager<GuildCreateListener> {
    }
}

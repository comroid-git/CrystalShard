package de.comroid.crystalshard.core.api.gateway.listener.guild;

import de.comroid.crystalshard.core.api.gateway.event.guild.GuildCreateEvent;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListenerManager;

public interface GuildCreateListener extends GatewayListener<GuildCreateEvent> {
    interface Manager extends GatewayListenerManager<GuildCreateListener> {
    }
}

package de.comroid.crystalshard.core.api.gateway.listener.guild.integration;

import de.comroid.crystalshard.core.api.gateway.event.guild.integration.GuildIntegrationsUpdateEvent;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListenerManager;

public interface GuildIntegrationsUpdateListener extends GatewayListener<GuildIntegrationsUpdateEvent> {
    interface Manager extends GatewayListenerManager<GuildIntegrationsUpdateListener> {
    }
}

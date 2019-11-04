package de.comroid.crystalshard.core.api.gateway.listener.guild.integration;

import de.comroid.crystalshard.core.api.gateway.event.GUILD_INTEGRATIONS_UPDATE;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListenerManager;

public interface GuildIntegrationsUpdateListener extends GatewayListener<GUILD_INTEGRATIONS_UPDATE> {
    interface Manager extends GatewayListenerManager<GuildIntegrationsUpdateListener> {
    }
}

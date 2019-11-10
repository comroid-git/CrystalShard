package de.comroid.crystalshard.core.gateway.listener.guild.integration;

import de.comroid.crystalshard.core.gateway.event.GUILD_INTEGRATIONS_UPDATE;
import de.comroid.crystalshard.core.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.gateway.listener.GatewayListenerManager;

public interface GuildIntegrationsUpdateListener extends GatewayListener<GUILD_INTEGRATIONS_UPDATE> {
    interface Manager extends GatewayListenerManager<GuildIntegrationsUpdateListener> {
    }
}

package de.kaleidox.crystalshard.core.api.gateway.listener.guild.integration;

import de.kaleidox.crystalshard.core.api.gateway.event.guild.integration.GuildIntegrationsUpdateEvent;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListener;

@FunctionalInterface
public interface GuildIntegrationsUpdateListener extends GatewayListener<GuildIntegrationsUpdateEvent> {
    void onGuildIntegrationUpdate(GuildIntegrationsUpdateEvent event);
}

package de.kaleidox.crystalshard.core.api.gateway.event.guild.integration;

// https://discordapp.com/developers/docs/topics/gateway#guild-integrations-update

import de.kaleidox.crystalshard.api.entity.guild.Guild;
import de.kaleidox.crystalshard.core.api.gateway.event.GatewayEvent;
import de.kaleidox.crystalshard.core.api.gateway.listener.guild.integration.GuildIntegrationsUpdateListener;
import de.kaleidox.crystalshard.util.annotation.ManagedBy;

@ManagedBy(GuildIntegrationsUpdateListener.Manager.class)
public interface GuildIntegrationsUpdateEvent extends GatewayEvent {
    String NAME = "GUILD_INTEGRATIONS_UPDATE";

    Guild getGuild();
}

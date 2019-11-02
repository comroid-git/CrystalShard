package de.comroid.crystalshard.core.api.gateway.event.guild.integration;

// https://discordapp.com/developers/docs/topics/gateway#guild-integrations-update

import de.comroid.crystalshard.api.entity.guild.Guild;
import de.comroid.crystalshard.core.api.gateway.event.GatewayEvent;
import de.comroid.crystalshard.core.api.gateway.listener.guild.integration.GuildIntegrationsUpdateListener;
import de.comroid.crystalshard.util.annotation.InitializedBy;

public interface GuildIntegrationsUpdateEvent extends GatewayEvent {
    String NAME = "GUILD_INTEGRATIONS_UPDATE";

    Guild getGuild();
}

package de.kaleidox.crystalshard.core.api.gateway.event.guild.integration;

// https://discordapp.com/developers/docs/topics/gateway#guild-integrations-update

import de.kaleidox.crystalshard.api.entity.guild.Guild;
import de.kaleidox.crystalshard.core.api.gateway.event.GatewayEvent;

public interface GuildIntegrationsUpdateEvent extends GatewayEvent {
    String NAME = "GUILD_INTEGRATIONS_UPDATE";

    Guild getGuild();
}

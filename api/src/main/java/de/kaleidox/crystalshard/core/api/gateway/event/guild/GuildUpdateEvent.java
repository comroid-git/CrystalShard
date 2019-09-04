package de.kaleidox.crystalshard.core.api.gateway.event.guild;

// https://discordapp.com/developers/docs/topics/gateway#guild-update

import de.kaleidox.crystalshard.api.entity.guild.Guild;
import de.kaleidox.crystalshard.core.api.gateway.event.GatewayEvent;

public interface GuildUpdateEvent extends GatewayEvent {
    String NAME = "GUILD_UPDATE";

    Guild getGuild();
}

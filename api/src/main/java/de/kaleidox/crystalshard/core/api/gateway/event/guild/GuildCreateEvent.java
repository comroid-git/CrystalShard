package de.kaleidox.crystalshard.core.api.gateway.event.guild;

// https://discordapp.com/developers/docs/topics/gateway#guild-create

import de.kaleidox.crystalshard.api.entity.guild.Guild;
import de.kaleidox.crystalshard.core.api.gateway.event.GatewayEvent;

public interface GuildCreateEvent extends GatewayEvent {
    String NAME = "GUILD_CREATE";

    Guild getGuild();
}

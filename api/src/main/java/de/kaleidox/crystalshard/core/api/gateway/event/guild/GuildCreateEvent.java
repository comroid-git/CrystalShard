package de.kaleidox.crystalshard.core.api.gateway.event.guild;

// https://discordapp.com/developers/docs/topics/gateway#guild-create

import de.kaleidox.crystalshard.api.entity.guild.Guild;
import de.kaleidox.crystalshard.core.api.gateway.event.GatewayEvent;
import de.kaleidox.crystalshard.core.api.gateway.listener.guild.GuildCreateListener;
import de.kaleidox.crystalshard.util.annotation.ManagedBy;

@ManagedBy(GuildCreateListener.Manager.class)
public interface GuildCreateEvent extends GatewayEvent {
    String NAME = "GUILD_CREATE";

    Guild getGuild();
}

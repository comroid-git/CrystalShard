package de.comroid.crystalshard.core.api.gateway.event.guild;

// https://discordapp.com/developers/docs/topics/gateway#guild-create

import de.comroid.crystalshard.api.entity.guild.Guild;
import de.comroid.crystalshard.core.api.gateway.event.GatewayEvent;
import de.comroid.crystalshard.core.api.gateway.listener.guild.GuildCreateListener;
import de.comroid.crystalshard.util.annotation.ManagedBy;

@ManagedBy(GuildCreateListener.Manager.class)
public interface GuildCreateEvent extends GatewayEvent {
    String NAME = "GUILD_CREATE";

    Guild getGuild();
}

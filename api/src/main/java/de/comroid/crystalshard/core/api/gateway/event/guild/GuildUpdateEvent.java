package de.comroid.crystalshard.core.api.gateway.event.guild;

// https://discordapp.com/developers/docs/topics/gateway#guild-update

import de.comroid.crystalshard.api.entity.guild.Guild;
import de.comroid.crystalshard.core.api.gateway.event.GatewayEvent;
import de.comroid.crystalshard.core.api.gateway.listener.guild.GuildUpdateListener;
import de.comroid.crystalshard.util.annotation.ManagedBy;

@ManagedBy(GuildUpdateListener.Manager.class)
public interface GuildUpdateEvent extends GatewayEvent {
    String NAME = "GUILD_UPDATE";

    Guild getGuild();
}

package de.comroid.crystalshard.core.api.gateway.event.guild;

// https://discordapp.com/developers/docs/topics/gateway#guild-delete

import de.comroid.crystalshard.api.entity.Snowflake;
import de.comroid.crystalshard.core.api.gateway.event.GatewayEvent;
import de.comroid.crystalshard.core.api.gateway.listener.guild.GuildDeleteListener;
import de.comroid.crystalshard.util.annotation.InitializedBy;

public interface GuildDeleteEvent extends GatewayEvent {
    String NAME = "GUILD_DELETE";

    Snowflake getGuildID();

    boolean isUnavailable();

    boolean isRemoved();
}

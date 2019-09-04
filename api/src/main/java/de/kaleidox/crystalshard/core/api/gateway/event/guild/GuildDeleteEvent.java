package de.kaleidox.crystalshard.core.api.gateway.event.guild;

// https://discordapp.com/developers/docs/topics/gateway#guild-delete

import de.kaleidox.crystalshard.api.entity.Snowflake;
import de.kaleidox.crystalshard.core.api.gateway.event.GatewayEvent;

public interface GuildDeleteEvent extends GatewayEvent {
    String NAME = "GUILD_DELETE";

    Snowflake getGuildID();

    boolean isUnavailable();

    boolean isRemoved();
}

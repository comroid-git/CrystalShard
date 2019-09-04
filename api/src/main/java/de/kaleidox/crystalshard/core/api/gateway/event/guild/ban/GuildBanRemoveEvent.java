package de.kaleidox.crystalshard.core.api.gateway.event.guild.ban;

// https://discordapp.com/developers/docs/topics/gateway#guild-ban-remove

import de.kaleidox.crystalshard.api.entity.guild.Guild;
import de.kaleidox.crystalshard.api.entity.user.User;
import de.kaleidox.crystalshard.core.api.gateway.event.GatewayEvent;

public interface GuildBanRemoveEvent extends GatewayEvent {
    String NAME = "GUILD_BAN_REMOVE";

    Guild getGuild();

    User getUser();
}

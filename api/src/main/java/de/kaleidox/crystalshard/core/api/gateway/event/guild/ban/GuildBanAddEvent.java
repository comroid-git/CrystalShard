package de.kaleidox.crystalshard.core.api.gateway.event.guild.ban;

// https://discordapp.com/developers/docs/topics/gateway#guild-ban-add

import de.kaleidox.crystalshard.api.entity.guild.Guild;
import de.kaleidox.crystalshard.api.entity.user.User;
import de.kaleidox.crystalshard.core.api.gateway.event.GatewayEvent;

public interface GuildBanAddEvent extends GatewayEvent {
    String NAME = "GUILD_BAN_ADD";

    Guild getGuild();

    User getUser();
}

package de.kaleidox.crystalshard.core.api.gateway.event.guild.ban;

// https://discordapp.com/developers/docs/topics/gateway#guild-ban-remove

import de.kaleidox.crystalshard.api.entity.guild.Guild;
import de.kaleidox.crystalshard.api.entity.user.User;
import de.kaleidox.crystalshard.core.api.gateway.event.GatewayEvent;
import de.kaleidox.crystalshard.core.api.gateway.listener.guild.ban.GuildBanRemoveListener;
import de.kaleidox.crystalshard.util.annotation.ManagedBy;

@ManagedBy(GuildBanRemoveListener.Manager.class)
public interface GuildBanRemoveEvent extends GatewayEvent {
    String NAME = "GUILD_BAN_REMOVE";

    Guild getGuild();

    User getUser();
}

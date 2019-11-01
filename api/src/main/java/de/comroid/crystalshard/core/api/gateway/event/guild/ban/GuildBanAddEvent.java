package de.comroid.crystalshard.core.api.gateway.event.guild.ban;

// https://discordapp.com/developers/docs/topics/gateway#guild-ban-add

import de.comroid.crystalshard.api.entity.guild.Guild;
import de.comroid.crystalshard.api.entity.user.User;
import de.comroid.crystalshard.core.api.gateway.event.GatewayEvent;
import de.comroid.crystalshard.core.api.gateway.listener.guild.ban.GuildBanAddListener;
import de.comroid.crystalshard.util.annotation.ManagedBy;

@ManagedBy(GuildBanAddListener.Manager.class)
public interface GuildBanAddEvent extends GatewayEvent {
    String NAME = "GUILD_BAN_ADD";

    Guild getGuild();

    User getUser();
}

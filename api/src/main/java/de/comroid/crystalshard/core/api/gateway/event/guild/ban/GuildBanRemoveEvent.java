package de.comroid.crystalshard.core.api.gateway.event.guild.ban;

// https://discordapp.com/developers/docs/topics/gateway#guild-ban-remove

import de.comroid.crystalshard.api.entity.guild.Guild;
import de.comroid.crystalshard.api.entity.user.User;
import de.comroid.crystalshard.core.api.gateway.event.GatewayEvent;
import de.comroid.crystalshard.core.api.gateway.listener.guild.ban.GuildBanRemoveListener;
import de.comroid.crystalshard.util.annotation.ManagedBy;

@ManagedBy(GuildBanRemoveListener.Manager.class)
public interface GuildBanRemoveEvent extends GatewayEvent {
    String NAME = "GUILD_BAN_REMOVE";

    Guild getGuild();

    User getUser();
}

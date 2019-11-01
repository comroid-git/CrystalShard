package de.comroid.crystalshard.core.api.gateway.event.guild.member;

// https://discordapp.com/developers/docs/topics/gateway#guild-member-remove

import de.comroid.crystalshard.api.entity.guild.Guild;
import de.comroid.crystalshard.api.entity.user.User;
import de.comroid.crystalshard.core.api.gateway.event.GatewayEvent;
import de.comroid.crystalshard.core.api.gateway.listener.guild.member.GuildMemberRemoveListener;
import de.comroid.crystalshard.util.annotation.ManagedBy;

@ManagedBy(GuildMemberRemoveListener.Manager.class)
public interface GuildMemberRemoveEvent extends GatewayEvent {
    String NAME = "GUILD_MEMBER_REMOVE";

    Guild getGuild();

    User getUser();
}

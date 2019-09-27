package de.kaleidox.crystalshard.core.api.gateway.event.guild.member;

// https://discordapp.com/developers/docs/topics/gateway#guild-member-add

import de.kaleidox.crystalshard.api.entity.guild.Guild;
import de.kaleidox.crystalshard.api.entity.user.GuildMember;
import de.kaleidox.crystalshard.core.api.gateway.event.GatewayEvent;
import de.kaleidox.crystalshard.core.api.gateway.listener.guild.member.GuildMemberAddListener;
import de.kaleidox.crystalshard.util.annotation.ManagedBy;

@ManagedBy(GuildMemberAddListener.Manager.class)
public interface GuildMemberAddEvent extends GatewayEvent {
    String NAME = "GUILD_MEMBER_ADD";

    Guild getGuild();

    GuildMember getMember();
}

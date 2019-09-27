package de.kaleidox.crystalshard.core.api.gateway.event.guild.member;

// https://discordapp.com/developers/docs/topics/gateway#guild-member-update

import java.util.Collection;
import java.util.Optional;

import de.kaleidox.crystalshard.api.entity.guild.Guild;
import de.kaleidox.crystalshard.api.entity.guild.Role;
import de.kaleidox.crystalshard.api.entity.user.GuildMember;
import de.kaleidox.crystalshard.core.api.gateway.event.GatewayEvent;
import de.kaleidox.crystalshard.core.api.gateway.listener.guild.member.GuildMemberUpdateListener;
import de.kaleidox.crystalshard.util.annotation.ManagedBy;

@ManagedBy(GuildMemberUpdateListener.Manager.class)
public interface GuildMemberUpdateEvent extends GatewayEvent {
    String NAME = "GUILD_MEMBER_UPDATE";

    Guild getGuild();

    Collection<Role> getRole();

    GuildMember getMember();

    Optional<String> getNickname();
}

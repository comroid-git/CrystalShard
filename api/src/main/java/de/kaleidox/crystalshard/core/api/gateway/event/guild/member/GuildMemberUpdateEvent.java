package de.kaleidox.crystalshard.core.api.gateway.event.guild.member;

// https://discordapp.com/developers/docs/topics/gateway#guild-member-update

import java.util.Collection;
import java.util.Optional;

import de.kaleidox.crystalshard.api.entity.guild.Guild;
import de.kaleidox.crystalshard.api.entity.guild.Role;
import de.kaleidox.crystalshard.api.entity.user.GuildMember;
import de.kaleidox.crystalshard.core.api.gateway.event.GatewayEvent;

public interface GuildMemberUpdateEvent extends GatewayEvent {
    String NAME = "GUILD_MEMBER_UPDATE";

    Guild getGuild();

    Collection<Role> getRole();

    Collection<GuildMember> getMember();

    Optional<String> getNickname();
}

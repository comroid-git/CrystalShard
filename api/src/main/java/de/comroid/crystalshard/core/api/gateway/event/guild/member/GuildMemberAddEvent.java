package de.comroid.crystalshard.core.api.gateway.event.guild.member;

// https://discordapp.com/developers/docs/topics/gateway#guild-member-add

import de.comroid.crystalshard.api.entity.guild.Guild;
import de.comroid.crystalshard.api.entity.user.GuildMember;
import de.comroid.crystalshard.core.api.gateway.event.GatewayEvent;
import de.comroid.crystalshard.core.api.gateway.listener.guild.member.GuildMemberAddListener;
import de.comroid.crystalshard.util.annotation.InitializedBy;

public interface GuildMemberAddEvent extends GatewayEvent {
    String NAME = "GUILD_MEMBER_ADD";

    Guild getGuild();

    GuildMember getMember();
}

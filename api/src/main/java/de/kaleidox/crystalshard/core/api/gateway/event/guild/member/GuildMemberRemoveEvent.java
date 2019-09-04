package de.kaleidox.crystalshard.core.api.gateway.event.guild.member;

// https://discordapp.com/developers/docs/topics/gateway#guild-member-remove

import de.kaleidox.crystalshard.api.entity.guild.Guild;
import de.kaleidox.crystalshard.api.entity.user.User;
import de.kaleidox.crystalshard.core.api.gateway.event.GatewayEvent;

public interface GuildMemberRemoveEvent extends GatewayEvent {
    String NAME = "GUILD_MEMBER_REMOVE";

    Guild getGuild();

    User getUser();
}

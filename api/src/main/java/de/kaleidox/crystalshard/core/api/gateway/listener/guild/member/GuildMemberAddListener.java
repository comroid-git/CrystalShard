package de.kaleidox.crystalshard.core.api.gateway.listener.guild.member;

import de.kaleidox.crystalshard.core.api.gateway.event.guild.member.GuildMemberAddEvent;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListener;

@FunctionalInterface
public interface GuildMemberAddListener extends GatewayListener<GuildMemberAddEvent> {
    void onGuildMemberAdd(GuildMemberAddEvent event);
}

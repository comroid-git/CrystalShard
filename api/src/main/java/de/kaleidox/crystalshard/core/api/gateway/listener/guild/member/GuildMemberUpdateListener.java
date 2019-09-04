package de.kaleidox.crystalshard.core.api.gateway.listener.guild.member;

import de.kaleidox.crystalshard.core.api.gateway.event.guild.member.GuildMemberUpdateEvent;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListener;

@FunctionalInterface
public interface GuildMemberUpdateListener extends GatewayListener<GuildMemberUpdateEvent> {
    void onGuildMemberUpdate(GuildMemberUpdateEvent event);
}

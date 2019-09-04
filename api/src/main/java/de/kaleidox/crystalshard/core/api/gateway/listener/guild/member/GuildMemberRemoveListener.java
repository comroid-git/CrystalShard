package de.kaleidox.crystalshard.core.api.gateway.listener.guild.member;

import de.kaleidox.crystalshard.core.api.gateway.event.guild.member.GuildMemberRemoveEvent;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListener;

@FunctionalInterface
public interface GuildMemberRemoveListener extends GatewayListener<GuildMemberRemoveEvent> {
    void onGuildMemberRemove(GuildMemberRemoveEvent event);
}

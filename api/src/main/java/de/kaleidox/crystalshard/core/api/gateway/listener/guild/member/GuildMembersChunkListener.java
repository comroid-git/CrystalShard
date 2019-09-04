package de.kaleidox.crystalshard.core.api.gateway.listener.guild.member;

import de.kaleidox.crystalshard.core.api.gateway.event.guild.member.GuildMembersChunkEvent;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListener;

@FunctionalInterface
public interface GuildMembersChunkListener extends GatewayListener<GuildMembersChunkEvent> {
    void onGuildMembersChunk(GuildMembersChunkEvent event);
}

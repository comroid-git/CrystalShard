package de.kaleidox.crystalshard.core.api.gateway.listener.guild.member;

import de.kaleidox.crystalshard.core.api.gateway.event.guild.member.GuildMembersChunkEvent;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListener;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListenerManager;

public interface GuildMembersChunkListener extends GatewayListener<GuildMembersChunkEvent> {
    interface Manager extends GatewayListenerManager<GuildMembersChunkListener> {
    }
}

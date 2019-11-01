package de.comroid.crystalshard.core.api.gateway.listener.guild.member;

import de.comroid.crystalshard.core.api.gateway.event.guild.member.GuildMembersChunkEvent;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListenerManager;

public interface GuildMembersChunkListener extends GatewayListener<GuildMembersChunkEvent> {
    interface Manager extends GatewayListenerManager<GuildMembersChunkListener> {
    }
}

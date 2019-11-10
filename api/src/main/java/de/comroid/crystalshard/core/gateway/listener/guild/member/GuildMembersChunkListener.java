package de.comroid.crystalshard.core.gateway.listener.guild.member;

import de.comroid.crystalshard.core.gateway.event.GUILD_MEMBERS_CHUNK;
import de.comroid.crystalshard.core.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.gateway.listener.GatewayListenerManager;

public interface GuildMembersChunkListener extends GatewayListener<GUILD_MEMBERS_CHUNK> {
    interface Manager extends GatewayListenerManager<GuildMembersChunkListener> {
    }
}

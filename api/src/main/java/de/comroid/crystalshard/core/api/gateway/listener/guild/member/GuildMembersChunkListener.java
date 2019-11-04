package de.comroid.crystalshard.core.api.gateway.listener.guild.member;

import de.comroid.crystalshard.core.api.gateway.event.GUILD_MEMBERS_CHUNK;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListenerManager;

public interface GuildMembersChunkListener extends GatewayListener<GUILD_MEMBERS_CHUNK> {
    interface Manager extends GatewayListenerManager<GuildMembersChunkListener> {
    }
}

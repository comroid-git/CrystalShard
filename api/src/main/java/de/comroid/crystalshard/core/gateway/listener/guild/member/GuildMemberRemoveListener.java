package de.comroid.crystalshard.core.gateway.listener.guild.member;

import de.comroid.crystalshard.core.gateway.event.GUILD_MEMBER_REMOVE;
import de.comroid.crystalshard.core.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.gateway.listener.GatewayListenerManager;

public interface GuildMemberRemoveListener extends GatewayListener<GUILD_MEMBER_REMOVE> {
    interface Manager extends GatewayListenerManager<GuildMemberRemoveListener> {
    }
}

package de.comroid.crystalshard.core.api.gateway.listener.guild.member;

import de.comroid.crystalshard.core.api.gateway.event.GUILD_MEMBER_REMOVE;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListenerManager;

public interface GuildMemberRemoveListener extends GatewayListener<GUILD_MEMBER_REMOVE> {
    interface Manager extends GatewayListenerManager<GuildMemberRemoveListener> {
    }
}

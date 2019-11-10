package de.comroid.crystalshard.core.gateway.listener.guild.member;

import de.comroid.crystalshard.core.gateway.event.GUILD_MEMBER_ADD;
import de.comroid.crystalshard.core.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.gateway.listener.GatewayListenerManager;

public interface GuildMemberAddListener extends GatewayListener<GUILD_MEMBER_ADD> {
    interface Manager extends GatewayListenerManager<GuildMemberAddListener> {
    }
}

package de.comroid.crystalshard.core.gateway.listener.guild.member;

import de.comroid.crystalshard.core.gateway.event.GUILD_MEMBER_UPDATE;
import de.comroid.crystalshard.core.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.gateway.listener.GatewayListenerManager;

public interface GuildMemberUpdateListener extends GatewayListener<GUILD_MEMBER_UPDATE> {
    interface Manager extends GatewayListenerManager<GuildMemberUpdateListener> {
    }
}

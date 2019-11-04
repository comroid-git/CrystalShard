package de.comroid.crystalshard.core.api.gateway.listener.guild.member;

import de.comroid.crystalshard.core.api.gateway.event.GUILD_MEMBER_UPDATE;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListenerManager;

public interface GuildMemberUpdateListener extends GatewayListener<GUILD_MEMBER_UPDATE> {
    interface Manager extends GatewayListenerManager<GuildMemberUpdateListener> {
    }
}

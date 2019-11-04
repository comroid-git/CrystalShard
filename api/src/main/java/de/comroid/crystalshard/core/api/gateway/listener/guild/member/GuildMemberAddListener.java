package de.comroid.crystalshard.core.api.gateway.listener.guild.member;

import de.comroid.crystalshard.core.api.gateway.event.GUILD_MEMBER_ADD;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListenerManager;

public interface GuildMemberAddListener extends GatewayListener<GUILD_MEMBER_ADD> {
    interface Manager extends GatewayListenerManager<GuildMemberAddListener> {
    }
}

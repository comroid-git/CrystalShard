package de.comroid.crystalshard.core.api.gateway.listener.guild.member;

import de.comroid.crystalshard.core.api.gateway.event.guild.member.GuildMemberRemoveEvent;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListenerManager;

public interface GuildMemberRemoveListener extends GatewayListener<GuildMemberRemoveEvent> {
    interface Manager extends GatewayListenerManager<GuildMemberRemoveListener> {
    }
}

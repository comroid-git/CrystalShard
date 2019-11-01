package de.comroid.crystalshard.core.api.gateway.listener.guild.member;

import de.comroid.crystalshard.core.api.gateway.event.guild.member.GuildMemberUpdateEvent;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListenerManager;

public interface GuildMemberUpdateListener extends GatewayListener<GuildMemberUpdateEvent> {
    interface Manager extends GatewayListenerManager<GuildMemberUpdateListener> {
    }
}

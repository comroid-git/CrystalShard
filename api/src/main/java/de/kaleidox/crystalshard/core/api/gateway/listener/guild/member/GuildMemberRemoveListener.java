package de.kaleidox.crystalshard.core.api.gateway.listener.guild.member;

import de.kaleidox.crystalshard.core.api.gateway.event.guild.member.GuildMemberRemoveEvent;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListener;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListenerManager;

public interface GuildMemberRemoveListener extends GatewayListener<GuildMemberRemoveEvent> {
    interface Manager extends GatewayListenerManager<GuildMemberRemoveListener> {
    }
}

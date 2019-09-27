package de.kaleidox.crystalshard.core.api.gateway.listener.guild.member;

import de.kaleidox.crystalshard.core.api.gateway.event.guild.member.GuildMemberAddEvent;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListener;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListenerManager;

public interface GuildMemberAddListener extends GatewayListener<GuildMemberAddEvent> {
    interface Manager extends GatewayListenerManager<GuildMemberAddListener> {
    }
}

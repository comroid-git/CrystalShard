package de.kaleidox.crystalshard.core.api.gateway.listener.guild.ban;

import de.kaleidox.crystalshard.core.api.gateway.event.guild.ban.GuildBanAddEvent;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListener;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListenerManager;

public interface GuildBanAddListener extends GatewayListener<GuildBanAddEvent> {
    interface Manager extends GatewayListenerManager<GuildBanAddListener> {
    }
}

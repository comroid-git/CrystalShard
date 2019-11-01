package de.comroid.crystalshard.core.api.gateway.listener.guild.ban;

import de.comroid.crystalshard.core.api.gateway.event.guild.ban.GuildBanAddEvent;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListenerManager;

public interface GuildBanAddListener extends GatewayListener<GuildBanAddEvent> {
    interface Manager extends GatewayListenerManager<GuildBanAddListener> {
    }
}

package de.kaleidox.crystalshard.core.api.gateway.listener.guild.ban;

import de.kaleidox.crystalshard.core.api.gateway.event.guild.ban.GuildBanRemoveEvent;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListener;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListenerManager;

public interface GuildBanRemoveListener extends GatewayListener<GuildBanRemoveEvent> {
    interface Manager extends GatewayListenerManager<GuildBanRemoveListener> {
    }
}

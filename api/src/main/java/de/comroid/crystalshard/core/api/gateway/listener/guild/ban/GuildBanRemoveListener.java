package de.comroid.crystalshard.core.api.gateway.listener.guild.ban;

import de.comroid.crystalshard.core.api.gateway.event.guild.ban.GuildBanRemoveEvent;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListenerManager;

public interface GuildBanRemoveListener extends GatewayListener<GuildBanRemoveEvent> {
    interface Manager extends GatewayListenerManager<GuildBanRemoveListener> {
    }
}

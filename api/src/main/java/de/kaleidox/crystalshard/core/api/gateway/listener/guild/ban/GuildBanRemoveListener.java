package de.kaleidox.crystalshard.core.api.gateway.listener.guild.ban;

import de.kaleidox.crystalshard.core.api.gateway.event.guild.ban.GuildBanRemoveEvent;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListener;

@FunctionalInterface
public interface GuildBanRemoveListener extends GatewayListener<GuildBanRemoveEvent> {
    void onGuildBanRemove(GuildBanRemoveEvent event);
}

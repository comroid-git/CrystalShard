package de.kaleidox.crystalshard.core.api.gateway.listener.guild;

import de.kaleidox.crystalshard.core.api.gateway.event.guild.GuildDeleteEvent;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListener;

@FunctionalInterface
public interface GuildDeleteListener extends GatewayListener<GuildDeleteEvent> {
    void onGuildDelete(GuildDeleteEvent event);
}

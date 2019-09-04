package de.kaleidox.crystalshard.core.api.gateway.listener.guild;

import de.kaleidox.crystalshard.core.api.gateway.event.guild.GuildUpdateEvent;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListener;

@FunctionalInterface
public interface GuildUpdateListener extends GatewayListener<GuildUpdateEvent> {
    void onGuildUpdate(GuildUpdateEvent event);
}

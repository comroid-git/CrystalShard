package de.kaleidox.crystalshard.core.api.gateway.listener.guild;

import de.kaleidox.crystalshard.core.api.gateway.event.guild.GuildCreateEvent;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListener;

@FunctionalInterface
public interface GuildCreateListener extends GatewayListener<GuildCreateEvent> {
    void onGuildCreate(GuildCreateEvent event);
}

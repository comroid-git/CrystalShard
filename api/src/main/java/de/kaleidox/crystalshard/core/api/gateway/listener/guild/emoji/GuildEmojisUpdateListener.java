package de.kaleidox.crystalshard.core.api.gateway.listener.guild.emoji;

import de.kaleidox.crystalshard.core.api.gateway.event.guild.emoji.GuildEmojisUpdateEvent;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListener;

@FunctionalInterface
public interface GuildEmojisUpdateListener extends GatewayListener<GuildEmojisUpdateEvent> {
    void onGuildEmojisUpdate(GuildEmojisUpdateEvent event);
}

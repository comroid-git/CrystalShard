package de.kaleidox.crystalshard.core.api.gateway.listener.guild.emoji;

import de.kaleidox.crystalshard.core.api.gateway.event.guild.emoji.GuildEmojisUpdateEvent;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListener;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListenerManager;

public interface GuildEmojisUpdateListener extends GatewayListener<GuildEmojisUpdateEvent> {
    interface Manager extends GatewayListenerManager<GuildEmojisUpdateListener> {
    }
}

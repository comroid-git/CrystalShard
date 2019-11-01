package de.comroid.crystalshard.core.api.gateway.listener.guild.emoji;

import de.comroid.crystalshard.core.api.gateway.event.guild.emoji.GuildEmojisUpdateEvent;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListenerManager;

public interface GuildEmojisUpdateListener extends GatewayListener<GuildEmojisUpdateEvent> {
    interface Manager extends GatewayListenerManager<GuildEmojisUpdateListener> {
    }
}

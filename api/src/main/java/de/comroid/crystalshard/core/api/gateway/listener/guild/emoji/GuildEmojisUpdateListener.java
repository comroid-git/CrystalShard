package de.comroid.crystalshard.core.api.gateway.listener.guild.emoji;

import de.comroid.crystalshard.core.api.gateway.event.GUILD_EMOJIS_UPDATE;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListenerManager;

public interface GuildEmojisUpdateListener extends GatewayListener<GUILD_EMOJIS_UPDATE> {
    interface Manager extends GatewayListenerManager<GuildEmojisUpdateListener> {
    }
}

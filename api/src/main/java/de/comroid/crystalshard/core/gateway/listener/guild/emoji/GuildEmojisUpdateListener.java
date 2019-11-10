package de.comroid.crystalshard.core.gateway.listener.guild.emoji;

import de.comroid.crystalshard.core.gateway.event.GUILD_EMOJIS_UPDATE;
import de.comroid.crystalshard.core.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.gateway.listener.GatewayListenerManager;

public interface GuildEmojisUpdateListener extends GatewayListener<GUILD_EMOJIS_UPDATE> {
    interface Manager extends GatewayListenerManager<GuildEmojisUpdateListener> {
    }
}

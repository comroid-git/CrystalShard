package de.comroid.crystalshard.core.api.gateway.event.guild.emoji;

// https://discordapp.com/developers/docs/topics/gateway#guild-emojis-update

import java.util.Collection;

import de.comroid.crystalshard.api.entity.emoji.CustomEmoji;
import de.comroid.crystalshard.api.entity.guild.Guild;
import de.comroid.crystalshard.core.api.gateway.event.GatewayEvent;
import de.comroid.crystalshard.core.api.gateway.listener.guild.emoji.GuildEmojisUpdateListener;
import de.comroid.crystalshard.util.annotation.InitializedBy;

public interface GuildEmojisUpdateEvent extends GatewayEvent {
    String NAME = "GUILD_EMOJIS_UPDATE";

    Guild getGuild();

    Collection<CustomEmoji> getEmojis();
}

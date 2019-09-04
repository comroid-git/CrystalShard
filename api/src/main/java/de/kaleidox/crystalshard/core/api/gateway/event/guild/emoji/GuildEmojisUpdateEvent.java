package de.kaleidox.crystalshard.core.api.gateway.event.guild.emoji;

// https://discordapp.com/developers/docs/topics/gateway#guild-emojis-update

import java.util.Collection;

import de.kaleidox.crystalshard.api.entity.emoji.CustomEmoji;
import de.kaleidox.crystalshard.api.entity.guild.Guild;
import de.kaleidox.crystalshard.core.api.gateway.event.GatewayEvent;

public interface GuildEmojisUpdateEvent extends GatewayEvent {
    String NAME = "GUILD_EMOJIS_UPDATE";

    Guild getGuild();

    Collection<CustomEmoji> getEmojis();
}

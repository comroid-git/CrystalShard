package de.kaleidox.crystalshard.internal.core.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.internal.DiscordInternal;

/**
 * https://discordapp.com/developers/docs/topics/gateway#channel-update
 */
public class CHANNEL_UPDATE extends HandlerBase {
    @Override
    public void handle(DiscordInternal discord, JsonNode data) {
        long id = data.get("id").asLong();
        discord.getChannelById(id);
    }
}

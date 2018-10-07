package de.kaleidox.crystalshard.internal.handling.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.internal.DiscordInternal;

/**
 * https://discordapp.com/developers/docs/topics/gateway#voice-server-update
 */
public class VOICE_SERVER_UPDATE extends HandlerBase {
    // Override Methods
    @Override
    public void handle(DiscordInternal discord, JsonNode data) {
        // This event will be used when implementing Voice Support.
    }
}

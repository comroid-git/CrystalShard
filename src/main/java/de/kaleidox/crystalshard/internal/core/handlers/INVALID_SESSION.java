package de.kaleidox.crystalshard.internal.core.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.internal.DiscordInternal;

/**
 * https://discordapp.com/developers/docs/topics/gateway#invalid-session
 */
public class INVALID_SESSION extends HandlerBase {
    @Override
    public void handle(DiscordInternal discord, JsonNode data) {
        baseLogger.error("Invalid Session! " + data);
    }
}

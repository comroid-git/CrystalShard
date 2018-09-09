package de.kaleidox.crystalshard.internal.core.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.internal.DiscordInternal;

/**
 * https://discordapp.com/developers/docs/topics/gateway#resumed
 */
public class RESUMED extends HandlerBase {
    @Override
    public void handle(DiscordInternal discord, JsonNode data) {
        baseLogger.traceElseInfo("Resumed session with trace data: " + data, "Resumed session.");
    }
}

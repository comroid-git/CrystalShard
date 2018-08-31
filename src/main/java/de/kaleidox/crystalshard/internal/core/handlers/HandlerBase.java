package de.kaleidox.crystalshard.internal.core.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.logging.Logger;

public abstract class HandlerBase {
    final static Logger baseLogger = new Logger(HandlerBase.class);
    public abstract void handle(JsonNode data);
}

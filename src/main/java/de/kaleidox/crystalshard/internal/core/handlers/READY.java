package de.kaleidox.crystalshard.internal.core.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.internal.DiscordInternal;

public class READY extends HandlerBase {
    @Override
    public void handle(JsonNode data) {
        System.out.println("recieved data at READY!");
    }
}

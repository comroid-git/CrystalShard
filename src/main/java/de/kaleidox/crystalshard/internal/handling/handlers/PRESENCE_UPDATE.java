package de.kaleidox.crystalshard.internal.handling.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.internal.DiscordInternal;

public class PRESENCE_UPDATE extends HandlerBase {
    @Override
    public void handle(DiscordInternal discord, JsonNode data) {
        System.out.println("recieved data at PRESENCE_UPDATE!");
    }
}

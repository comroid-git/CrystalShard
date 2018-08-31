package de.kaleidox.crystalshard.internal.core.handlers;

import com.fasterxml.jackson.databind.JsonNode;

public class GUILD_CREATE extends HandlerBase {
    @Override
    public void handle(JsonNode data) {
        System.out.println("recieved data at GUILD_CREATE!");
    }
}

package de.kaleidox.crystalshard.internal.core.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.internal.DiscordInternal;

public class GUILD_CREATE extends HandlerBase {
    @Override
    public void handle(DiscordInternal discord, JsonNode data) {
        System.out.println("recieved data at GUILD_CREATE!");
    }
}

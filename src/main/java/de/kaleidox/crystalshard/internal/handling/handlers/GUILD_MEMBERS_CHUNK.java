package de.kaleidox.crystalshard.internal.handling.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.internal.DiscordInternal;

public class GUILD_MEMBERS_CHUNK extends HandlerBase {
    // Override Methods
    @Override
    public void handle(DiscordInternal discord, JsonNode data) {
        // todo Response to Request Guild Members
        // https://discordapp.com/developers/docs/topics/gateway#request-guild-members
    }
}

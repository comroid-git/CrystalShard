package de.kaleidox.crystalshard.internal.core.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.items.user.SelfInternal;

public class READY extends HandlerBase {
    @Override
    public void handle(DiscordInternal discord, JsonNode data) {
        discord.setSelf(new SelfInternal(discord, data.get("user")));
    }
}

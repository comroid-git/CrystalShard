package de.kaleidox.crystalshard.internal.handling.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.handling.event.server.generic.ServerDeleteEventInternal;
import de.kaleidox.crystalshard.internal.items.server.ServerInternal;
import de.kaleidox.crystalshard.main.handling.listener.server.generic.ServerDeleteListener;
import de.kaleidox.crystalshard.main.items.server.Server;

public class GUILD_DELETE extends HandlerBase {
    // Override Methods
    @Override
    public void handle(DiscordInternal discord, JsonNode data) {
        Server server = ServerInternal.getInstance(discord,
                                                   data.get("id")
                                                           .asLong());
        boolean gotKicked = (!data.has("unavailable") || data.get("unavailable")
                .isNull());
        
        ServerDeleteEventInternal event = new ServerDeleteEventInternal(discord, server.getId(), gotKicked);
        
        collectListeners(ServerDeleteListener.class, discord, server).forEach(listener -> discord.getThreadPool()
                .execute(() -> listener.onServerDelete(event)));
        
        server.detachAllListeners();
    }
}

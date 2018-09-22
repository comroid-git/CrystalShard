package de.kaleidox.crystalshard.internal.handling.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.handling.event.server.generic.ServerCreateEventInternal;
import de.kaleidox.crystalshard.internal.items.server.ServerInternal;
import de.kaleidox.crystalshard.main.handling.event.server.generic.ServerCreateEvent;
import de.kaleidox.crystalshard.main.handling.listener.server.generic.ServerCreateListener;
import de.kaleidox.crystalshard.main.items.server.Server;

public class GUILD_CREATE extends HandlerBase {
    // Override Methods
    @Override
    public void handle(DiscordInternal discord, JsonNode data) {
        Server server = ServerInternal.getInstance(discord, data);
        
        ServerCreateEvent event = new ServerCreateEventInternal(discord, server);
        discord.addServer(server);
        
        collectListeners(ServerCreateListener.class, discord).forEach(listener -> listener.onServerCreate(event));
    }
}

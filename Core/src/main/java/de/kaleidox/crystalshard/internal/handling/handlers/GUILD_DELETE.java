package de.kaleidox.crystalshard.internal.handling.handlers;

import com.fasterxml.jackson.databind.JsonNode;

import de.kaleidox.crystalshard.api.entity.server.Server;
import de.kaleidox.crystalshard.api.handling.listener.server.generic.ServerDeleteListener;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.handling.event.server.generic.ServerDeleteEventInternal;

import java.util.concurrent.TimeUnit;

public class GUILD_DELETE extends HandlerBase {
    // Override Methods
    @Override
    public void handle(DiscordInternal discord, JsonNode data) {
        long serverId = data.get("id")
                .asLong();
        Server server = discord.getServerCache()
                .getOrNull(serverId);
        boolean gotKicked = (!data.has("unavailable") || data.get("unavailable")
                .isNull());

        ServerDeleteEventInternal event = new ServerDeleteEventInternal(discord, server.getId(), gotKicked);

        collectListeners(ServerDeleteListener.class, discord, server).forEach(listener -> discord.getThreadPool()
                .execute(() -> listener.onServerDelete(event)));

        server.detachAllListeners();
        discord.getThreadPool()
                .getScheduler()
                .schedule(() -> discord.getServerCache()
                        .destroyFromCache(serverId), 30, TimeUnit.MINUTES);
    }
}

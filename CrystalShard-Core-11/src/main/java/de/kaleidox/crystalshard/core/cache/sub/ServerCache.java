package de.kaleidox.crystalshard.core.cache.sub;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.core.cache.Cache;
import de.kaleidox.crystalshard.core.net.request.Endpoint;
import de.kaleidox.crystalshard.core.net.request.Method;
import de.kaleidox.crystalshard.core.net.request.WebRequest;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.items.server.ServerInternal;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.util.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class ServerCache extends Cache<Server, Long, Long> {
    private final DiscordInternal discord;
    
    public ServerCache(DiscordInternal discord) {
        super(ServerInternal.class, param -> ((JsonNode) param[1]).get("id").asLong(), TimeUnit.DAYS.toMillis(7), Discord.class, JsonNode.class);
        this.discord = discord;
    }
    
    // Override Methods
    @NotNull
    @Override
    public CompletableFuture<Object[]> requestConstructorParameters(Long requestIdent) {
        return new WebRequest<Object[]>(discord).method(Method.GET)
                .endpoint(Endpoint.Location.GUILD_SPECIFIC.toEndpoint(requestIdent))
                .execute(node -> new Object[]{discord, node});
    }
    
    @NotNull
    @Override
    public Server construct(Object... param) {
        return new ServerInternal((Discord) param[0], (JsonNode) param[1]);
    }
}

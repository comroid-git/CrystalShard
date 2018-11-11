package de.kaleidox.crystalshard.core.cache.sub;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.core.CoreDelegate;
import de.kaleidox.crystalshard.core.cache.CacheImpl;
import de.kaleidox.crystalshard.core.net.request.HttpMethod;
import de.kaleidox.crystalshard.core.net.request.WebRequest;
import de.kaleidox.crystalshard.core.net.request.endpoint.DiscordEndpoint;
import de.kaleidox.crystalshard.internal.InternalDelegate;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.util.annotations.NotNull;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class ServerCacheImpl extends CacheImpl<Server, Long, Long> {
    private final Discord discord;

    public ServerCacheImpl(Discord discord) {
        super(Server.class, param -> ((JsonNode) param[1]).get("id")
                .asLong(), TimeUnit.DAYS.toMillis(7), Discord.class, JsonNode.class);
        this.discord = discord;
    }

    @NotNull
    @Override
    public CompletableFuture<Object[]> requestConstructorParameters(Long requestIdent) {
        WebRequest<Object[]> request = CoreDelegate.webRequest(discord);
        return request.setMethod(HttpMethod.GET)
                .setUri(DiscordEndpoint.GUILD_SPECIFIC.createUri(requestIdent))
                .executeAs(node -> new Object[]{discord, node});
    }

    @NotNull
    @Override
    public Server construct(Object... param) {
        return InternalDelegate.newInstance(Server.class, param);
    }
}

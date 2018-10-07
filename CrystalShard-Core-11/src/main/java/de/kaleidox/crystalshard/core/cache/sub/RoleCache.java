package de.kaleidox.crystalshard.core.cache.sub;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.core.cache.Cache;
import de.kaleidox.crystalshard.core.cache.Cacheable;
import de.kaleidox.crystalshard.core.net.request.Endpoint;
import de.kaleidox.crystalshard.core.net.request.Method;
import de.kaleidox.crystalshard.core.net.request.WebRequest;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.items.role.RoleInternal;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.items.role.Role;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.util.annotations.NotNull;
import de.kaleidox.crystalshard.util.objects.markers.IDPair;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class RoleCache extends Cache<Role, Long, IDPair> {
    private final DiscordInternal discord;
    
    public RoleCache(DiscordInternal discordInternal) {
        super(RoleInternal.class,
              param -> ((JsonNode) param[2]).get("id")
                      .asLong(),
              TimeUnit.HOURS.toMillis(6),
              Discord.class,
              Server.class,
              JsonNode.class);
        this.discord = discordInternal;
    }
    
    // Override Methods
    @NotNull
    @Override
    public CompletableFuture<Object[]> requestConstructorParameters(IDPair requestIdent) {
        Server server = Cacheable.getInstance(Server.class, requestIdent.getOne());
        return new WebRequest<Object[]>(discord).method(Method.GET)
                .endpoint(Endpoint.Location.GUILD_ROLES.toEndpoint(requestIdent))
                .execute(node -> {
                    for (JsonNode role : node) {
                        if (role.get("id")
                                    .asLong() == requestIdent.getTwo()) return new Object[]{discord, server, role};
                    }
                    throw new NoSuchElementException("Error fetching role information.");
                });
    }
    
    @NotNull
    @Override
    public Role construct(Object... param) {
        return new RoleInternal((Discord) param[0], (Server) param[1], (JsonNode) param[2]);
    }
}

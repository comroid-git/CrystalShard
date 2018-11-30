package de.kaleidox.crystalshard.core.cache.sub;

import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.NotNull;

import de.kaleidox.crystalshard.core.CoreInjector;
import de.kaleidox.crystalshard.core.cache.CacheImpl;
import de.kaleidox.crystalshard.core.cache.Cacheable;
import de.kaleidox.crystalshard.core.net.request.HttpMethod;
import de.kaleidox.crystalshard.core.net.request.WebRequest;
import de.kaleidox.crystalshard.core.net.request.endpoint.DiscordEndpoint;
import de.kaleidox.crystalshard.internal.InternalInjector;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.items.role.Role;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.util.objects.markers.IDPair;

import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class RoleCacheImpl extends CacheImpl<Role, Long, IDPair> {
    private final Discord discord;

    public RoleCacheImpl(Discord discordInternal) {
        super(Role.class, param -> ((JsonNode) param[2]).get("id")
                .asLong(), TimeUnit.HOURS.toMillis(6), Discord.class, Server.class, JsonNode.class);
        this.discord = discordInternal;
    }

    // Override Methods
    @NotNull
    @Override
    public CompletableFuture<Object[]> requestConstructorParameters(IDPair requestIdent) {
        Server server = Cacheable.getInstance(Server.class, requestIdent.getOne());
        WebRequest<Object[]> request = CoreInjector.webRequest(discord);
        return request.setMethod(HttpMethod.GET)
                .setUri(DiscordEndpoint.GUILD_ROLES.createUri(requestIdent))
                .executeAs(node -> {
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
        return InternalInjector.newInstance(Role.class, param);
    }
}

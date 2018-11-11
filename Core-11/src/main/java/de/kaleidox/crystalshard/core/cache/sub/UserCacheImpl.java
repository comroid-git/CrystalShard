package de.kaleidox.crystalshard.core.cache.sub;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.core.CoreInjector;
import de.kaleidox.crystalshard.core.cache.CacheImpl;
import de.kaleidox.crystalshard.core.net.request.HttpMethod;
import de.kaleidox.crystalshard.core.net.request.WebRequest;
import de.kaleidox.crystalshard.core.net.request.endpoint.DiscordEndpoint;
import de.kaleidox.crystalshard.internal.InternalInjector;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.items.user.User;
import de.kaleidox.util.annotations.NotNull;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class UserCacheImpl extends CacheImpl<User, Long, Long> {
    private final Discord discordInternal;

    public UserCacheImpl(Discord discordInternal) {
        super(User.class, param -> ((JsonNode) param[1]).get("id")
                .asLong(), TimeUnit.HOURS.toMillis(12), Discord.class, JsonNode.class);
        this.discordInternal = discordInternal;
    }

    @NotNull
    @Override
    public CompletableFuture<Object[]> requestConstructorParameters(Long requestIdent) {
        WebRequest<Object[]> request = CoreInjector.webRequest(discordInternal);
        return request.setMethod(HttpMethod.GET)
                .setUri(DiscordEndpoint.USER.createUri(requestIdent))
                .executeAs(node -> new Object[]{discordInternal, node});
    }

    @NotNull
    @Override
    public User construct(Object... param) {
        return InternalInjector.newInstance(User.class, param);
    }
}

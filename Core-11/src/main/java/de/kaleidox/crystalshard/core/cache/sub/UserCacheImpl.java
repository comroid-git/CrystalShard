package de.kaleidox.crystalshard.core.cache.sub;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.core.CoreDelegate;
import de.kaleidox.crystalshard.core.cache.CacheImpl;
import de.kaleidox.crystalshard.core.net.request.Endpoint;
import de.kaleidox.crystalshard.core.net.request.Method;
import de.kaleidox.crystalshard.core.net.request.WebRequest;
import de.kaleidox.crystalshard.internal.InternalDelegate;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.items.user.User;
import de.kaleidox.crystalshard.util.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class UserCacheImpl extends CacheImpl<User, Long, Long> {
    private final Discord discordInternal;
    
    public UserCacheImpl(Discord discordInternal) {
        super(User.class, param -> ((JsonNode) param[1]).get("id").asLong(), TimeUnit.HOURS.toMillis(12), Discord.class, JsonNode.class);
        this.discordInternal = discordInternal;
    }
    
    @NotNull
    @Override
    public CompletableFuture<Object[]> requestConstructorParameters(Long requestIdent) {
        WebRequest<Object[]> request = CoreDelegate.webRequest(discordInternal);
        return request.method(Method.GET)
                .endpoint(Endpoint.Location.USER.toEndpoint(requestIdent))
                .execute(node -> new Object[]{discordInternal, node});
    }
    
    @NotNull
    @Override
    public User construct(Object... param) {
        return InternalDelegate.newInstance(User.class, param);
    }
}
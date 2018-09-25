package de.kaleidox.crystalshard.core.cache.sub;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.core.cache.Cache;
import de.kaleidox.crystalshard.core.net.request.Endpoint;
import de.kaleidox.crystalshard.core.net.request.Method;
import de.kaleidox.crystalshard.core.net.request.WebRequest;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.items.user.UserInternal;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.items.user.User;
import de.kaleidox.util.ImageEndpoint;
import de.kaleidox.util.annotations.NotNull;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class UserCache extends Cache<User, Long, Long> {
    private final DiscordInternal discordInternal;
    
    public UserCache(DiscordInternal discordInternal) {
        super(UserInternal.class, TimeUnit.HOURS.toMillis(12), Discord.class, JsonNode.class);
        this.discordInternal = discordInternal;
    }
    
    @NotNull
    @Override
    public CompletableFuture<Object[]> requestConstructorParameters(Long requestIdent) {
        return new WebRequest<Object[]>(discordInternal)
                .method(Method.GET)
                .endpoint(Endpoint.Location.USER.toEndpoint(requestIdent))
                .execute(node -> new Object[]{discordInternal, node});
    }
    
    @NotNull
    @Override
    public User construct(Object... parameters) {
        return new UserInternal((Discord) parameters[0], (JsonNode) parameters[1]);
    }
}

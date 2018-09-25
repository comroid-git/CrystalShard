package de.kaleidox.crystalshard.core.cache.sub;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.core.cache.Cache;
import de.kaleidox.crystalshard.core.cache.Cacheable;
import de.kaleidox.crystalshard.core.net.request.Endpoint;
import de.kaleidox.crystalshard.core.net.request.Method;
import de.kaleidox.crystalshard.core.net.request.WebRequest;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.items.server.emoji.CustomEmojiInternal;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.items.server.emoji.CustomEmoji;
import de.kaleidox.util.annotations.NotNull;
import de.kaleidox.util.objects.markers.IDPair;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class EmojiCache extends Cache<CustomEmoji, Long, IDPair> {
    private final DiscordInternal discord;
    
    public EmojiCache(DiscordInternal discord) {
        super(CustomEmojiInternal.class,
              param -> ((JsonNode) param[1]).get("id")
                      .asLong(),
              TimeUnit.HOURS.toMillis(6),
              Discord.class,
              Server.class,
              JsonNode.class,
              Boolean.class);
        this.discord = discord;
    }
    
    // Override Methods
    @NotNull
    @Override
    public CompletableFuture<Object[]> requestConstructorParameters(IDPair requestIdent) {
        Server server = Cacheable.getInstance(Server.class, requestIdent.getOne());
        return new WebRequest<Object[]>(discord).method(Method.GET)
                .endpoint(Endpoint.Location.CUSTOM_EMOJI_SPECIFIC.toEndpoint(requestIdent))
                .execute(node -> new Object[]{discord, server, node, true});
    }
    
    @NotNull
    @Override
    public CustomEmoji construct(Object... param) {
        return new CustomEmojiInternal((Discord) param[0], (Server) param[1], (JsonNode) param[2], (Boolean) param[3]);
    }
}

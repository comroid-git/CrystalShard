package de.kaleidox.crystalshard.core.cache.sub;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.core.CoreDelegate;
import de.kaleidox.crystalshard.core.cache.CacheImpl;
import de.kaleidox.crystalshard.core.cache.Cacheable;
import de.kaleidox.crystalshard.core.net.request.HttpMethod;
import de.kaleidox.crystalshard.core.net.request.WebRequest;
import de.kaleidox.crystalshard.core.net.request.endpoint.DiscordEndpoint;
import de.kaleidox.crystalshard.internal.InternalDelegate;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.items.server.emoji.CustomEmoji;
import de.kaleidox.util.annotations.NotNull;
import de.kaleidox.util.objects.markers.IDPair;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class EmojiCacheImpl extends CacheImpl<CustomEmoji, Long, IDPair> {
    private final Discord discord;

    public EmojiCacheImpl(Discord discord) {
        super(CustomEmoji.class,
                param -> ((JsonNode) param[2]).get("id")
                        .asLong(),
                TimeUnit.HOURS.toMillis(6),
                Discord.class,
                Server.class,
                JsonNode.class,
                Boolean.class);
        this.discord = discord;
    }

    @NotNull
    @Override
    public CompletableFuture<Object[]> requestConstructorParameters(IDPair requestIdent) {
        Server server = Cacheable.getInstance(Server.class, requestIdent.getOne());
        WebRequest<Object[]> request = CoreDelegate.webRequest(discord);
        return request.setMethod(HttpMethod.GET)
                .setUri(DiscordEndpoint.CUSTOM_EMOJI_SPECIFIC.createUri(requestIdent))
                .executeAs(node -> new Object[]{discord, server, node, true});
    }

    @NotNull
    @Override
    public CustomEmoji construct(Object... param) {
        return InternalDelegate.newInstance(CustomEmoji.class, param);
    }
}

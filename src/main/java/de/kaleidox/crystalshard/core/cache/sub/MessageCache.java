package de.kaleidox.crystalshard.core.cache.sub;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.core.cache.Cache;
import de.kaleidox.crystalshard.core.net.request.Endpoint;
import de.kaleidox.crystalshard.core.net.request.Method;
import de.kaleidox.crystalshard.core.net.request.WebRequest;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.items.message.MessageInternal;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.items.message.Message;
import de.kaleidox.util.annotations.NotNull;
import de.kaleidox.util.objects.markers.IDPair;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class MessageCache extends Cache<Message, Long, IDPair> {
    private final DiscordInternal discordInternal;
    
    public MessageCache(DiscordInternal discordInternal) {
        super(MessageInternal.class,
              param -> ((JsonNode) param[1]).get("id")
                      .asLong(),
              TimeUnit.HOURS.toMillis(12),
              Discord.class,
              JsonNode.class);
        this.discordInternal = discordInternal;
    }
    
    // Override Methods
    @NotNull
    @Override
    public CompletableFuture<Object[]> requestConstructorParameters(IDPair idPair) {
        return new WebRequest<Object[]>(discordInternal).method(Method.GET)
                .endpoint(Endpoint.Location.MESSAGE_SPECIFIC.toEndpoint(idPair.getOne(), idPair.getTwo()))
                .execute(node -> new Object[]{discordInternal, node});
    }
    
    @NotNull
    @Override
    public Message construct(Object... param) {
        return new MessageInternal((Discord) param[0], (JsonNode) param[1]);
    }
}

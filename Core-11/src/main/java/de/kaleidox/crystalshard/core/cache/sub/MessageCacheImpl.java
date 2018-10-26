package de.kaleidox.crystalshard.core.cache.sub;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.core.CoreDelegate;
import de.kaleidox.crystalshard.core.cache.CacheImpl;
import de.kaleidox.crystalshard.core.net.request.Endpoint;
import de.kaleidox.crystalshard.core.net.request.Method;
import de.kaleidox.crystalshard.core.net.request.WebRequest;
import de.kaleidox.crystalshard.internal.InternalDelegate;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.items.message.Message;
import de.kaleidox.crystalshard.util.annotations.NotNull;
import de.kaleidox.crystalshard.util.objects.markers.IDPair;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class MessageCacheImpl extends CacheImpl<Message, Long, IDPair> {
    private final Discord discordInternal;
    
    public MessageCacheImpl(Discord discordInternal) {
        super(Message.class, param -> ((JsonNode) param[1]).get("id").asLong(), TimeUnit.HOURS.toMillis(12), Discord.class, JsonNode.class);
        this.discordInternal = discordInternal;
    }
    
    // Override Methods
    @NotNull
    @Override
    public CompletableFuture<Object[]> requestConstructorParameters(IDPair idPair) {
        WebRequest<Object[]> request = CoreDelegate.webRequest(discordInternal);
        return request.method(Method.GET)
                .endpoint(Endpoint.Location.MESSAGE_SPECIFIC.toEndpoint(idPair.getOne(), idPair.getTwo()))
                .execute(node -> new Object[]{discordInternal, node});
    }
    
    @NotNull
    @Override
    public Message construct(Object... param) {
        return InternalDelegate.newInstance(Message.class, param);
    }
}

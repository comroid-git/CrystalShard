package de.kaleidox.crystalshard.core.cache.sub;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.core.cache.Cache;
import de.kaleidox.crystalshard.internal.items.message.MessageInternal;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.items.message.Message;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class MessageCache extends Cache<Message, Long> {
    private MessageCache() {
        super(MessageInternal.class, TimeUnit.HOURS.toMillis(12), Discord.class, JsonNode.class);
    }
    
    @Override
    public CompletableFuture<Message> request(Long ident) {
        return null;
    }
    
    @Override
    public Message get(Long aLong) {
        return null;
    }
    
    @Override
    public Message getOrCreate(Long aLong, Object... defaultConstructorValues) {
        return null;
    }
    
    @Override
    public Message construct(Object... parameters) {
        return null;
    }
}

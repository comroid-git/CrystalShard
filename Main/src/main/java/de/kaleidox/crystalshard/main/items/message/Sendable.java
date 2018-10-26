package de.kaleidox.crystalshard.main.items.message;

import de.kaleidox.crystalshard.internal.InternalDelegate;
import de.kaleidox.crystalshard.main.items.message.embed.Embed;

import java.util.concurrent.CompletableFuture;

public interface Sendable {
    Sendable add(Object item);
    
    Sendable add(String string);
    
    Sendable add(Embed embed);
    
    CompletableFuture<Message> send(MessageReciever reciever);
    
    static Sendable of(Object... items) {
        Sendable sendable = get();
        for (Object item : items) sendable.add(item);
        return sendable;
    }
    
    static Sendable get() {
        return InternalDelegate.newInstance(Sendable.class);
    }
}

package de.kaleidox.crystalshard.main.items.message;

import de.kaleidox.crystalshard.internal.items.message.SendableInternal;
import de.kaleidox.crystalshard.main.items.message.embed.Embed;
import java.util.concurrent.CompletableFuture;

public interface Sendable {
    Sendable add(Object item);
    
    Sendable add(String string);
    
    Sendable add(Embed embed);
    
    CompletableFuture<Message> send(MessageReciever reciever);
    
    // Static members
    // Static membe
    static Sendable of(Object... items) {
        return new SendableInternal(items);
    }
    
    static Sendable get() {
        return new SendableInternal();
    }
}

package de.kaleidox.crystalshard.main.items.message;

import java.util.concurrent.CompletableFuture;

public interface MessageReciever {
    default CompletableFuture<Message> sendMessage(Sendable content) {
        return null;
    }
}

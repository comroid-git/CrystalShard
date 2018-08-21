package de.kaleidox.crystalshard.main.event;

import de.kaleidox.crystalshard.main.items.message.Message;

import java.util.concurrent.CompletableFuture;

public interface MessageEvent {
    Message getMessage();

    CompletableFuture<Message> requestMessage();
}

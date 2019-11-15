package de.comroid.crystalshard.api.event.multipart.message;

import java.util.concurrent.CompletableFuture;

import de.comroid.crystalshard.api.entity.message.Message;
import de.comroid.crystalshard.api.event.multipart.APIEvent;

import org.jetbrains.annotations.Contract;

public interface RequestMessageEvent extends MessageEvent, APIEvent {
    @Override
    @Contract 
    default Message getMessage() {
        return requestMessage().join();
    }

    CompletableFuture<Message> requestMessage();
}

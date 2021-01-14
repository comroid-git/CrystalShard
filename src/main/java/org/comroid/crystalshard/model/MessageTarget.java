package org.comroid.crystalshard.model;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.entity.channel.TextChannel;
import org.comroid.crystalshard.entity.message.Message;

import java.util.concurrent.CompletableFuture;

public interface MessageTarget extends ContextualProvider {
    CompletableFuture<TextChannel> getTargetChannel();

    default CompletableFuture<Message> sendText(String text) {
        return null; // todo
    }
}

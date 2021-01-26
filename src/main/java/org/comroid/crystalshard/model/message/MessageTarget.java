package org.comroid.crystalshard.model.message;

import org.comroid.crystalshard.Context;
import org.comroid.crystalshard.entity.message.Message;
import org.comroid.crystalshard.model.message.embed.Embed;
import org.jetbrains.annotations.ApiStatus.Internal;

import java.util.concurrent.CompletableFuture;

public interface MessageTarget extends Context {
    default MessageBuilder composeMessage() {
        return new MessageBuilder(this);
    }

    default MessageBuilder.EmbedComposer composeEmbed() {
        return composeMessage().embed();
    }

    default CompletableFuture<Message> sendText(String format, Object... args) {
        return sendText(String.format(format, args));
    }

    default CompletableFuture<Message> sendText(String text) {
        return composeMessage()
                .setContent(text)
                .compose();
    }

    default CompletableFuture<Message> sendEmbed(Embed embed) {
        return composeMessage()
                .setEmbed(embed)
                .compose();
    }

    @Internal
    CompletableFuture<Message> executeMessage(MessageBuilder builder);
}

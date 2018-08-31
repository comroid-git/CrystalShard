package de.kaleidox.crystalshard.main.items.message;

import de.kaleidox.crystalshard.main.items.message.embed.Embed;
import de.kaleidox.crystalshard.main.items.message.embed.EmbedDraft;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public interface MessageReciever {
    CompletableFuture<Message> sendMessage(Sendable content);

    CompletableFuture<Message> sendMessage(Consumer<Embed.Builder> defaultEmbedModifier);

    CompletableFuture<Message> sendMessage(EmbedDraft embedDraft);

    CompletableFuture<Message> sendMessage(String content);

    CompletableFuture<Void> typing();
}

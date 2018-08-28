package de.kaleidox.crystalshard.main.items.message;

import de.kaleidox.crystalshard.main.items.message.embed.Embed;
import de.kaleidox.crystalshard.main.items.message.embed.EmbedDraft;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;

public interface MessageReciever {
    default CompletableFuture<Message> sendMessage(Sendable content) {
        return null; // todo
    }

    default CompletableFuture<Message> sendMessage(Consumer<Embed.Builder> defaultEmbedModifier) {
        return null; // todo
    }
}

package de.kaleidox.crystalshard.main.items.message;

import de.kaleidox.crystalshard.main.items.DiscordItem;
import de.kaleidox.crystalshard.main.items.message.embed.Embed;
import de.kaleidox.crystalshard.main.items.message.embed.EmbedDraft;
import de.kaleidox.crystalshard.util.DefaultEmbed;
import de.kaleidox.util.annotations.Range;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public interface MessageReciever extends DiscordItem {
    CompletableFuture<Message> sendMessage(Sendable content);

    CompletableFuture<Message> sendMessage(String content);

    CompletableFuture<Void> typing();

    default CompletableFuture<Collection<Message>> getMessages() {
        return getMessages(50);
    }

    CompletableFuture<Collection<Message>> getMessages(@Range(min = 1, max = 100) int limit);

    default CompletableFuture<Message> sendMessage(Consumer<Embed.Builder> defaultEmbedModifier) {
        return sendMessage(DefaultEmbed.getStatic(defaultEmbedModifier)); // TODO: 08.11.2018 Create interface
    }

    CompletableFuture<Message> sendMessage(EmbedDraft embedDraft);
}

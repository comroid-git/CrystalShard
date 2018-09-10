package de.kaleidox.crystalshard.main.items.message;

import de.kaleidox.crystalshard.main.items.DiscordItem;
import de.kaleidox.crystalshard.main.items.message.embed.Embed;
import de.kaleidox.crystalshard.main.items.message.embed.EmbedDraft;
import de.kaleidox.crystalshard.util.DefaultEmbed;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public interface MessageReciever extends DiscordItem {
    CompletableFuture<Message> sendMessage(Sendable content);

    default CompletableFuture<Message> sendMessage(Consumer<Embed.Builder> defaultEmbedModifier) {
        return sendMessage(DefaultEmbed.getStatic(defaultEmbedModifier));
    }

    CompletableFuture<Message> sendMessage(EmbedDraft embedDraft);

    CompletableFuture<Message> sendMessage(String content);

    CompletableFuture<Void> typing();

    ScheduledFuture<Void> typeFor(long time, TimeUnit unit);

    Collection<Message> getMessages();
}

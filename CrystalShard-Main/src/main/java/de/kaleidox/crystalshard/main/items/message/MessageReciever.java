package de.kaleidox.crystalshard.main.items.message;

import de.kaleidox.crystalshard.main.items.DiscordItem;
import de.kaleidox.crystalshard.main.items.message.embed.Embed;
import de.kaleidox.crystalshard.main.items.message.embed.EmbedDraft;
import util.DefaultEmbed;
import util.annotations.Range;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public interface MessageReciever extends DiscordItem {
    CompletableFuture<Message> sendMessage(Sendable content);
    
    CompletableFuture<Message> sendMessage(EmbedDraft embedDraft);
    
    CompletableFuture<Message> sendMessage(String content);
    
    CompletableFuture<Void> typing();
    
    CompletableFuture<Collection<Message>> getMessages(@Range(min = 1, max = 100) int limit);
    
    default CompletableFuture<Collection<Message>> getMessages() {
        return getMessages(50);
    }
    
    default CompletableFuture<Message> sendMessage(Consumer<Embed.Builder> defaultEmbedModifier) {
        return sendMessage(DefaultEmbed.getStatic(defaultEmbedModifier));
    }
}

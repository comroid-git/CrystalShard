package de.kaleidox.crystalshard.main.items.message;

import de.kaleidox.crystalshard.main.items.DiscordItem;
import de.kaleidox.crystalshard.main.items.message.embed.Embed;
import de.kaleidox.crystalshard.main.items.message.embed.EmbedDraft;
import de.kaleidox.crystalshard.util.DefaultEmbed;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public interface MessageReciever extends DiscordItem {
    CompletableFuture<Message> sendMessage(Sendable content);
    
    CompletableFuture<Message> sendMessage(EmbedDraft embedDraft);
    
    CompletableFuture<Message> sendMessage(String content);
    
    CompletableFuture<Void> typing();
    
    Collection<Message> getMessages();
    
    default CompletableFuture<Message> sendMessage(Consumer<Embed.Builder> defaultEmbedModifier) {
        return sendMessage(DefaultEmbed.getStatic(defaultEmbedModifier));
    }
}

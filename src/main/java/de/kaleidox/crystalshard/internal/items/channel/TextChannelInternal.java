package de.kaleidox.crystalshard.internal.items.channel;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.kaleidox.crystalshard.core.net.request.Endpoint;
import de.kaleidox.crystalshard.core.net.request.Method;
import de.kaleidox.crystalshard.core.net.request.WebRequest;
import de.kaleidox.crystalshard.internal.items.message.MessageInternal;
import de.kaleidox.crystalshard.internal.items.message.SendableInternal;
import de.kaleidox.crystalshard.internal.items.message.embed.EmbedDraftInternal;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.items.channel.TextChannel;
import de.kaleidox.crystalshard.main.items.message.Message;
import de.kaleidox.crystalshard.main.items.message.Sendable;
import de.kaleidox.crystalshard.main.items.message.embed.Embed;
import de.kaleidox.crystalshard.main.items.message.embed.EmbedDraft;
import de.kaleidox.util.helpers.JsonHelper;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public abstract class TextChannelInternal extends ChannelInternal implements TextChannel {
    final ConcurrentHashMap<Long, Message> messages;
    
    TextChannelInternal(Discord discord, JsonNode data) {
        super(discord, data);
        
        messages = new ConcurrentHashMap<>();
    }
    
// Override Methods
    @Override
    public CompletableFuture<Message> sendMessage(Sendable content) {
        return new WebRequest<Message>(discord).method(Method.POST)
                .endpoint(Endpoint.Location.MESSAGE.toEndpoint(this))
                .node(((SendableInternal) content).toJsonNode(JsonHelper.objectNode()))
                .execute(node -> {
                    Message message = MessageInternal.getInstance(discord, node);
                    messages.put(message.getId(), message);
                    return message;
                });
    }
    
    @Override
    public CompletableFuture<Message> sendMessage(Consumer<Embed.Builder> defaultEmbedModifier) {
        Embed.Builder builder = discord.getUtilities().getDefaultEmbed().getBuilder();
        defaultEmbedModifier.accept(builder);
        return sendMessage(builder.build());
    }
    
    @Override
    public CompletableFuture<Message> sendMessage(EmbedDraft embedDraft) {
        ObjectNode data = JsonHelper.objectNode();
        data.set("content", JsonHelper.nodeOf(""));
        data.set("embed", ((EmbedDraftInternal) embedDraft).toJsonNode(JsonHelper.objectNode()));
        data.set("file", JsonHelper.nodeOf("content"));
        return new WebRequest<Message>(discord).method(Method.POST)
                .endpoint(Endpoint.Location.MESSAGE.toEndpoint(this))
                .node(data)
                .execute(node -> {
                    Message message = MessageInternal.getInstance(discord, node);
                    messages.put(message.getId(), message);
                    return message;
                });
    }
    
    @Override
    public CompletableFuture<Message> sendMessage(String content) {
        ObjectNode data = JsonHelper.objectNode();
        data.set("content", JsonHelper.nodeOf(content));
        data.set("file", JsonHelper.nodeOf("content"));
        return new WebRequest<Message>(discord).method(Method.POST)
                .endpoint(Endpoint.Location.MESSAGE.toEndpoint(this))
                .node(data)
                .execute(node -> {
                    Message message = MessageInternal.getInstance(discord, node);
                    messages.put(message.getId(), message);
                    return message;
                });
    }
    
    @Override
    public CompletableFuture<Void> typing() {
        return new WebRequest<Void>(discord).method(Method.POST).endpoint(Endpoint.Location.CHANNEL_TYPING.toEndpoint(
                this)).execute(node -> null);
    }
    
    @Override
    public Collection<Message> getMessages() {
        return null;
    }
}

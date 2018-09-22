package de.kaleidox.crystalshard.internal.items.channel;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.core.net.request.Endpoint;
import de.kaleidox.crystalshard.core.net.request.Method;
import de.kaleidox.crystalshard.core.net.request.WebRequest;
import de.kaleidox.crystalshard.internal.items.message.MessageInternal;
import de.kaleidox.crystalshard.internal.items.message.SendableInternal;
import de.kaleidox.crystalshard.internal.items.message.embed.EmbedDraftInternal;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.exception.DiscordPermissionException;
import de.kaleidox.crystalshard.main.items.channel.ServerChannel;
import de.kaleidox.crystalshard.main.items.channel.TextChannel;
import de.kaleidox.crystalshard.main.items.message.Message;
import de.kaleidox.crystalshard.main.items.message.Sendable;
import de.kaleidox.crystalshard.main.items.message.embed.Embed;
import de.kaleidox.crystalshard.main.items.message.embed.EmbedDraft;
import de.kaleidox.crystalshard.main.items.permission.Permission;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import static de.kaleidox.util.helpers.JsonHelper.*;

public abstract class TextChannelInternal extends ChannelInternal implements TextChannel {
    final ConcurrentHashMap<Long, Message> messages;
    
    TextChannelInternal(Discord discord, JsonNode data) {
        super(discord, data);
        
        messages = new ConcurrentHashMap<>();
    }
    
    // Override Methods
    @Override
    public CompletableFuture<Message> sendMessage(Sendable content) {
        if (checkPermissions()) return CompletableFuture.failedFuture(new DiscordPermissionException(
                "Sending Message to Text Channel [" + id + "])",
                Permission.SEND_MESSAGES));
        return new WebRequest<Message>(discord).method(Method.POST)
                .endpoint(Endpoint.Location.MESSAGE.toEndpoint(this))
                .node(((SendableInternal) content).toJsonNode(objectNode()))
                .execute(node -> {
                    Message message = MessageInternal.getInstance(discord, node);
                    messages.put(message.getId(), message);
                    return message;
                });
    }
    
    @Override
    public CompletableFuture<Message> sendMessage(Consumer<Embed.Builder> defaultEmbedModifier) {
        if (checkPermissions()) return CompletableFuture.failedFuture(new DiscordPermissionException(
                "Sending Message to Text Channel [" + id + "])",
                Permission.SEND_MESSAGES));
        Embed.Builder builder = discord.getUtilities().getDefaultEmbed().getBuilder();
        defaultEmbedModifier.accept(builder);
        return sendMessage(builder.build());
    }
    
    @Override
    public CompletableFuture<Message> sendMessage(EmbedDraft embedDraft) {
        if (checkPermissions()) return CompletableFuture.failedFuture(new DiscordPermissionException(
                "Sending Message to Text Channel [" + id + "])",
                Permission.SEND_MESSAGES));
        return new WebRequest<Message>(discord).method(Method.POST)
                .endpoint(Endpoint.Location.MESSAGE.toEndpoint(this))
                .node(objectNode("content",
                                 "",
                                 "embed",
                                 ((EmbedDraftInternal) embedDraft).toJsonNode(objectNode()),
                                 "file",
                                 "content"))
                .execute(node -> {
                    Message message = MessageInternal.getInstance(discord, node);
                    messages.put(message.getId(), message);
                    return message;
                });
    }
    
    @Override
    public CompletableFuture<Message> sendMessage(String content) {
        if (checkPermissions()) return CompletableFuture.failedFuture(new DiscordPermissionException(
                "Sending Message to Text Channel [" + id + "])",
                Permission.SEND_MESSAGES));
        return new WebRequest<Message>(discord).method(Method.POST)
                .endpoint(Endpoint.Location.MESSAGE.toEndpoint(this))
                .node(objectNode("content", content, "file", "content"))
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
    
    private boolean checkPermissions() {
        return (!toServerChannel().map(ServerChannel::getServer).isEmpty() && !hasPermission(discord.getSelf(),
                                                                                             Permission.SEND_MESSAGES));
    }
}

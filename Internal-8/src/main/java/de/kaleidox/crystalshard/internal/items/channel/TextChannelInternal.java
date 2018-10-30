package de.kaleidox.crystalshard.internal.items.channel;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.core.CoreDelegate;
import de.kaleidox.crystalshard.core.net.request.Endpoint;
import de.kaleidox.crystalshard.core.net.request.Method;
import de.kaleidox.crystalshard.core.net.request.WebRequest;
import de.kaleidox.crystalshard.internal.items.message.SendableInternal;
import de.kaleidox.crystalshard.internal.items.message.embed.EmbedDraftInternal;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.exception.DiscordPermissionException;
import de.kaleidox.crystalshard.main.items.channel.TextChannel;
import de.kaleidox.crystalshard.main.items.message.Message;
import de.kaleidox.crystalshard.main.items.message.Sendable;
import de.kaleidox.crystalshard.main.items.message.embed.Embed;
import de.kaleidox.crystalshard.main.items.message.embed.EmbedDraft;
import de.kaleidox.crystalshard.main.items.permission.Permission;

import de.kaleidox.crystalshard.util.helpers.FutureHelper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static de.kaleidox.crystalshard.util.helpers.JsonHelper.objectNode;

public abstract class TextChannelInternal extends ChannelInternal implements TextChannel {
    final ConcurrentHashMap<Long, Message> messages;
    final ConcurrentHashMap<Long, Message> pinned;
    
    TextChannelInternal(Discord discord, JsonNode data) {
        super(discord, data);
        
        messages = new ConcurrentHashMap<>();
        pinned = new ConcurrentHashMap<>();
    }
    
    // Override Methods
    @Override
    public CompletableFuture<Message> sendMessage(Sendable content) {
        if (checkPermissions()) return FutureHelper.failedFuture(new DiscordPermissionException("Sending Message to Text Channel [" + id + "])",
                                                                                                     Permission.SEND_MESSAGES));
        return CoreDelegate.webRequest(Message.class, discord).method(Method.POST)
                .endpoint(Endpoint.Location.MESSAGE.toEndpoint(this))
                .node(((SendableInternal) content).toJsonNode(objectNode()))
                .execute(node -> {
                    Message message = discord.getMessageCache().getOrCreate(discord, node);
                    messages.put(message.getId(), message);
                    return message;
                });
    }
    
    @Override
    public CompletableFuture<Message> sendMessage(Consumer<Embed.Builder> defaultEmbedModifier) {
        if (checkPermissions()) return FutureHelper.failedFuture(new DiscordPermissionException("Sending Message to Text Channel [" + id + "])",
                                                                                                     Permission.SEND_MESSAGES));
        Embed.Builder builder = discord.getUtilities().getDefaultEmbed().getBuilder();
        defaultEmbedModifier.accept(builder);
        return sendMessage(builder.build());
    }
    
    @Override
    public CompletableFuture<Message> sendMessage(EmbedDraft embedDraft) {
        if (checkPermissions()) return FutureHelper.failedFuture(new DiscordPermissionException("Sending Message to Text Channel [" + id + "])",
                                                                                                     Permission.SEND_MESSAGES));
        return CoreDelegate.webRequest(Message.class, discord).method(Method.POST).endpoint(Endpoint.Location.MESSAGE.toEndpoint(this)).node(objectNode("content",
                                                                                                                                         "",
                                                                                                                                         "embed",
                                                                                                                                         ((EmbedDraftInternal) embedDraft)
                                                                                                                                                 .toJsonNode(
                                                                                                                                                         objectNode()),
                                                                                                                                         "file",
                                                                                                                                         "content")).execute(
                node -> {
                    Message message = discord.getMessageCache().getOrCreate(discord, node);
                    messages.put(message.getId(), message);
                    return message;
                });
    }
    
    @Override
    public CompletableFuture<Message> sendMessage(String content) {
        if (checkPermissions()) return FutureHelper.failedFuture(new DiscordPermissionException("Sending Message to Text Channel [" + id + "])",
                                                                                                     Permission.SEND_MESSAGES));
        return CoreDelegate.webRequest(Message.class, discord).method(Method.POST).endpoint(Endpoint.Location.MESSAGE.toEndpoint(this)).node(objectNode("content",
                                                                                                                                         content,
                                                                                                                                         "file",
                                                                                                                                         "content")).execute(
                node -> {
                    Message message = discord.getMessageCache().getOrCreate(discord, node);
                    messages.put(message.getId(), message);
                    return message;
                });
    }
    
    @Override
    public CompletableFuture<Void> typing() {
        return CoreDelegate.webRequest(discord).method(Method.POST).endpoint(Endpoint.Location.CHANNEL_TYPING.toEndpoint(this)).executeNull();
    }
    
    @Override
    public CompletableFuture<Collection<Message>> getMessages(int limit) {
        if (limit < 1 || limit > 100) throw new IllegalArgumentException("Parameter 'limit' is not within its bounds! [1, 100]");
        if (!hasPermission(discord, Permission.READ_MESSAGES)) return FutureHelper.failedFuture(new DiscordPermissionException("Cannot read " + toString(),
                                                                                                                                    Permission.READ_MESSAGES));
        if (!hasPermission(discord, Permission.READ_MESSAGE_HISTORY)) return CompletableFuture.completedFuture(Collections.emptyList());
        WebRequest<Collection<Message>> request = CoreDelegate.webRequest(discord);
        return request.method(Method.GET)
                .endpoint(Endpoint.Location.MESSAGE.toEndpoint(id))
                .node(objectNode("limit",
                                 limit))
                .execute(data -> {
                    List<Message> list = new ArrayList<>();
                    data.forEach(msg -> list.add(discord.getMessageCache().getOrCreate(discord, msg)));
                    return list;
                });
    }
    
    @Override
    public Collection<Message> getPinnedMessages() {
        return pinned.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toList());
    }
    
    private boolean checkPermissions() {
        return (!isPrivate && !hasPermission(discord.getSelf(), Permission.SEND_MESSAGES));
    }
    
    public void updatePinned(Message message) {
        if (message.isPinned()) pinned.put(message.getId(), message);
        else pinned.remove(message.getId());
    }
}

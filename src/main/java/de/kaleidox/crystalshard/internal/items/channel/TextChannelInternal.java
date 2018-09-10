package de.kaleidox.crystalshard.internal.items.channel;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.kaleidox.crystalshard.core.net.request.Endpoint;
import de.kaleidox.crystalshard.core.net.request.Method;
import de.kaleidox.crystalshard.core.net.request.WebRequest;
import de.kaleidox.crystalshard.internal.items.message.MessageInternal;
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
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public abstract class TextChannelInternal extends ChannelInternal implements TextChannel {
    private final Discord discord;
    private final long id;

    TextChannelInternal(Discord discord, JsonNode json) {
        super(discord, json);
        this.discord = discord;
        this.id = json.get("id").asLong();

        discord.getThreadPool()
                .execute(() -> new WebRequest<Void>(discord)
                        .method(Method.GET)
                        .endpoint(Endpoint.Location.MESSAGE.toEndpoint(id))
                        .node(JsonHelper.objectNode("limit", 25))
                        .execute(node -> {
                            node.forEach(data -> MessageInternal.getInstance(discord, data));
                            return null;
                        }), "Fill message cache for " + this);
    }

    @Override
    public CompletableFuture<Message> sendMessage(Sendable content) {
        return null;
    }

    @Override
    public CompletableFuture<Message> sendMessage(Consumer<Embed.Builder> defaultEmbedModifier) {
        return null;
    }

    @Override
    public CompletableFuture<Message> sendMessage(EmbedDraft embedDraft) {
        ObjectNode data = JsonHelper.objectNode();
        data.set("content", JsonHelper.nodeOf(""));
        data.set("embed", ((EmbedDraftInternal) embedDraft).toJsonNode(JsonHelper.objectNode()));
        data.set("file", JsonHelper.nodeOf("content"));
        return new WebRequest<Message>(discord)
                .method(Method.POST)
                .endpoint(Endpoint.of(Endpoint.Location.MESSAGE, this))
                .node(data)
                .execute(node -> MessageInternal.getInstance(discord, node));
    }

    @Override
    public CompletableFuture<Message> sendMessage(String content) {
        ObjectNode data = JsonHelper.objectNode();
        data.set("content", JsonHelper.nodeOf(content));
        data.set("file", JsonHelper.nodeOf("content"));
        return new WebRequest<Message>(discord)
                .method(Method.POST)
                .endpoint(Endpoint.of(Endpoint.Location.MESSAGE, this))
                .node(data)
                .execute(node -> MessageInternal.getInstance(discord, node));
    }

    @Override
    public CompletableFuture<Void> typing() {
        return null;
    }

    @Override
    public ScheduledFuture<Void> typeFor(long time, TimeUnit unit) {
        return null; // todo
    }

    @Override
    public Collection<Message> getMessages() {
        return null;
    }
}

package de.kaleidox.crystalshard.internal.items.channel;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.kaleidox.crystalshard.internal.core.net.request.Endpoint;
import de.kaleidox.crystalshard.internal.core.net.request.Method;
import de.kaleidox.crystalshard.internal.core.net.request.WebRequest;
import de.kaleidox.crystalshard.internal.items.message.MessageInternal;
import de.kaleidox.crystalshard.internal.items.message.embed.EmbedDraftInternal;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.handling.listener.channel.ChannelAttachableListener;
import de.kaleidox.crystalshard.main.handling.listener.listener.MessageCreateListener;
import de.kaleidox.crystalshard.main.items.DiscordItem;
import de.kaleidox.crystalshard.main.items.channel.ChannelCategory;
import de.kaleidox.crystalshard.main.items.channel.ServerTextChannel;
import de.kaleidox.crystalshard.main.items.message.Message;
import de.kaleidox.crystalshard.main.items.message.Sendable;
import de.kaleidox.crystalshard.main.items.message.embed.EmbedDraft;
import de.kaleidox.crystalshard.main.items.permission.PermissionList;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.logging.Logger;
import de.kaleidox.util.helpers.JsonHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class ServerTextChannelInternal extends ChannelInternal implements ServerTextChannel {
    private final static Logger logger = new Logger(ServerTextChannelInternal.class);
    private final Discord discord;
    private final Server server;
    private final long id;
    private final String name;
    private final List<Message> messages = new ArrayList<>();
    private final List<ChannelAttachableListener> listeners = new ArrayList<>();

    public ServerTextChannelInternal(Discord discord, Server server, JsonNode data) {
        super(discord, data);
        logger.deeptrace("Creating STC object for data: " + data.toString());
        this.discord = discord;
        this.server = server;
        this.id = data.get("id").asLong();
        this.name = data.path("name").asText(null);
    }

    @Override
    public Server getServer() {
        return server;
    }

    @Override
    public Optional<ChannelCategory> getCategory() {
        return Optional.empty();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public PermissionList getListFor(DiscordItem scope) {
        return null;
    }

    @Override
    public CompletableFuture<Message> sendMessage(Sendable content) {
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
                .execute(node -> new MessageInternal(getDiscord(), getServer(), node));
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
                .execute(node -> new MessageInternal(getDiscord(), getServer(), node));
    }

    @Override
    public CompletableFuture<Void> typing() {
        return null;
    }

    public List<ChannelAttachableListener> getListeners() {
        return listeners;
    }

    public Message craftMessage(JsonNode data) {
        MessageInternal messageInternal = new MessageInternal(discord, server, data);
        this.messages.add(messageInternal);
        return messageInternal;
    }

    @Override
    public void attachMessageCreateListener(MessageCreateListener listener) {
        listeners.add(listener);
    }
}

package de.kaleidox.crystalshard.internal.items.channel;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.kaleidox.crystalshard.core.net.request.Endpoint;
import de.kaleidox.crystalshard.core.net.request.Method;
import de.kaleidox.crystalshard.core.net.request.WebRequest;
import de.kaleidox.crystalshard.internal.items.message.MessageInternal;
import de.kaleidox.crystalshard.internal.items.message.embed.EmbedDraftInternal;
import de.kaleidox.crystalshard.internal.items.server.ServerInternal;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.handling.listener.ListenerManager;
import de.kaleidox.crystalshard.main.handling.listener.channel.ChannelAttachableListener;
import de.kaleidox.crystalshard.main.handling.listener.message.MessageCreateListener;
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
import de.kaleidox.util.objects.Evaluation;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class ServerTextChannelInternal extends ChannelInternal implements ServerTextChannel {
    final static ConcurrentHashMap<Long, ServerTextChannel> instances = new ConcurrentHashMap<>();
    private final static Logger logger = new Logger(ServerTextChannelInternal.class);
    private final Discord discord;
    private final Server server;
    private final long id;
    private final String name;
    private final List<Message> messages = new ArrayList<>();
    private final List<ChannelAttachableListener> listeners = new ArrayList<>();

    private ServerTextChannelInternal(Discord discord, Server server, JsonNode data) {
        super(discord, data);
        logger.deeptrace("Creating STC object for data: " + data.toString());
        this.discord = discord;
        this.server = server;
        this.id = data.get("id").asLong();
        this.name = data.path("name").asText(null);
        instances.put(id, this);
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
                .execute(node -> MessageInternal.getInstance(discord, server, node));
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
                .execute(node -> MessageInternal.getInstance(discord, server, node));
    }

    @Override
    public CompletableFuture<Void> typing() {
        return null;
    }

    public List<? extends ChannelAttachableListener> getListeners() {
        return listeners;
    }

    @Override
    public void attachMessageCreateListener(MessageCreateListener listener) {
        listeners.add(listener);
    }

    @Override
    public <C extends ChannelAttachableListener> ListenerManager<C> attachListener(C listener) {
        return null;
    }

    @Override
    public Evaluation<Boolean> detachListener(ChannelAttachableListener listener) {
        return null;
    }

    @Override
    public String toString() {
        return "ServerTextChannel with ID [" + id + "]";
    }

    public static ServerTextChannel getInstance(Discord discord, Server server, JsonNode data) {
        long id = data.get("id").asLong(-1);
        if (id == -1) throw new NoSuchElementException("No valid ID found.");
        if (server == null) server = ServerInternal.getInstance(discord, data.path("guild_id").asLong(0));
        if (instances.containsKey(id))
            return instances.get(id);
        else
            return new ServerTextChannelInternal(discord, server, data);
    }
}

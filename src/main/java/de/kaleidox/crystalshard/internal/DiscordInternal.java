package de.kaleidox.crystalshard.internal;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.internal.core.concurrent.ThreadPool;
import de.kaleidox.crystalshard.internal.core.net.request.ratelimiting.Ratelimiting;
import de.kaleidox.crystalshard.internal.core.net.socket.WebSocketClient;
import de.kaleidox.crystalshard.internal.event.ListenerManagerInternal;
import de.kaleidox.crystalshard.internal.items.server.ServerInternal;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.handling.listener.DiscordAttachableListener;
import de.kaleidox.crystalshard.main.handling.listener.ListenerManager;
import de.kaleidox.crystalshard.main.handling.listener.message.MessageCreateListener;
import de.kaleidox.crystalshard.main.handling.listener.server.ServerCreateListener;
import de.kaleidox.crystalshard.main.items.channel.Channel;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.items.user.AccountType;
import de.kaleidox.crystalshard.main.items.user.Self;
import de.kaleidox.crystalshard.main.items.user.User;
import de.kaleidox.crystalshard.util.DiscordUtils;
import de.kaleidox.logging.Logger;
import de.kaleidox.util.objects.Evaluation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

public class DiscordInternal implements Discord {
    private final static Logger logger = new Logger(DiscordInternal.class);
    private final ThreadPool pool;
    private final String token;
    private final AccountType type;
    private final WebSocketClient webSocket;
    private final Ratelimiting ratelimiter;
    private final List<Server> servers;
    private final DiscordUtils utils;
    private final Collection<ListenerManager<? extends DiscordAttachableListener>> listenerManangers = new ArrayList<>();
    private final int thisShard;
    private final int shardCount;
    private Self self;
    private CompletableFuture<Void> selfFuture;

    public DiscordInternal(String token, AccountType type, int thisShard, int ShardCount) {
        selfFuture = new CompletableFuture<>();
        this.thisShard = thisShard;
        this.shardCount = ShardCount;
        this.pool = new ThreadPool(this);
        this.token = token;
        Logger.addBlankedWord(token);
        this.type = type;
        this.ratelimiter = new Ratelimiting(this);
        this.webSocket = new WebSocketClient(this);
        this.utils = new DiscordUtils();

        servers = new ArrayList<>();

        try {
            long waitMs = 2000;
            logger.info("Waiting for initialization to finish... (" + (waitMs / 1000) + "s)");
            selfFuture.join();
            Thread.sleep(waitMs);
            logger.info("Discord connection for user " + self.getDiscriminatedName() + " is ready!");
        } catch (InterruptedException e) {
            logger.exception(e);
        }
    }

    public DiscordInternal(String token) {
        this.pool = new ThreadPool(this, 1, "GetShards Discord Pool");
        this.token = token;
        this.type = AccountType.BOT;
        this.webSocket = null;
        this.ratelimiter = new Ratelimiting(this);
        this.servers = null;
        this.utils = null;
        this.thisShard = 0;
        this.shardCount = 1;
    }

    public ThreadPool getThreadPool() {
        return pool;
    }

    @Override
    public ListenerManager<MessageCreateListener> attachMessageCreateListener(MessageCreateListener listener) {
        return attachListener(listener);
    }

    public Collection<DiscordAttachableListener> getListenerManangers() {
        return listenerManangers.stream()
                .map(ListenerManager::getListener)
                .collect(Collectors.toList());
    }

    @Override
    public String getPrefixedToken() {
        return type.getPrefix() + token;
    }

    @Override
    public int getShardId() {
        return thisShard;
    }

    @Override
    public int getShards() {
        return shardCount;
    }

    @Override
    public ListenerManager<ServerCreateListener> attachServerCreateListener(ServerCreateListener listener) {
        return null; // todo
    }

    @Override
    public DiscordUtils getUtilities() {
        return utils;
    }

    @Override
    public Optional<Channel> getChannelById(long id) {
        return servers.stream()
                .flatMap(server -> server.getChannels().stream())
                .filter(channel -> channel.getId() == id)
                .map(Channel.class::cast)
                .findAny();
    }

    @Override
    public Optional<User> getUserById(long id) {
        return Optional.empty();
    }

    @Override
    public Self getSelf() {
        return self;
    }

    public void setSelf(Self self) {
        this.self = self;
        selfFuture.complete(null);
    }

    @Override
    public Optional<Server> getServerById(long id) {
        return servers.stream()
                .filter(server -> server.getId() == id)
                .findAny();
    }

    @Override
    public Executor getExecutor() {
        return getThreadPool().getExecutor();
    }

    public WebSocketClient getWebSocket() {
        return webSocket;
    }

    public Ratelimiting getRatelimiter() {
        return ratelimiter;
    }

    public void craftServer(JsonNode data) {
        this.servers.add(new ServerInternal(this, data));
    }

    public Collection<ListenerManager<? extends DiscordAttachableListener>> getAllListenerManagers() {
        return listenerManangers;
    }

    @Override
    public long getId() {
        return 0;
    }

    @Override
    public Discord getDiscord() {
        return this;
    }

    @Override
    public Evaluation<Boolean> detachListener(DiscordAttachableListener listener) {
        return Evaluation.of(listenerManangers.removeIf(manager -> manager.getListener().equals(listener)));
    }

    @Override
    public <T extends DiscordAttachableListener> ListenerManager<T> attachListener(T listener) {
        ListenerManagerInternal<T> manager = ListenerManagerInternal.getInstance(this, listener);
        // manager.addAttached(this);
        listenerManangers.add(manager);
        return manager;
    }
}

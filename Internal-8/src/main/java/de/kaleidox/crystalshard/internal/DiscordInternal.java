package de.kaleidox.crystalshard.internal;

import de.kaleidox.crystalshard.core.CoreDelegate;
import de.kaleidox.crystalshard.core.cache.Cache;
import de.kaleidox.crystalshard.core.concurrent.ThreadPool;
import de.kaleidox.crystalshard.core.net.request.ratelimiting.Ratelimiter;
import de.kaleidox.crystalshard.core.net.socket.WebSocketClient;
import de.kaleidox.crystalshard.internal.handling.ListenerManagerInternal;
import de.kaleidox.crystalshard.logging.Logger;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.handling.listener.DiscordAttachableListener;
import de.kaleidox.crystalshard.main.handling.listener.ListenerManager;
import de.kaleidox.crystalshard.main.items.channel.Channel;
import de.kaleidox.crystalshard.main.items.message.Message;
import de.kaleidox.crystalshard.main.items.role.Role;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.items.server.emoji.CustomEmoji;
import de.kaleidox.crystalshard.main.items.user.AccountType;
import de.kaleidox.crystalshard.main.items.user.Self;
import de.kaleidox.crystalshard.main.items.user.User;
import de.kaleidox.crystalshard.util.discord.DiscordUtils;
import de.kaleidox.crystalshard.util.objects.functional.Evaluation;
import de.kaleidox.crystalshard.util.objects.functional.LivingInt;
import de.kaleidox.crystalshard.util.objects.markers.IDPair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class DiscordInternal implements Discord {
    private final static Logger logger = new Logger(DiscordInternal.class);
    private final ThreadPool pool;
    private final String token;
    private final AccountType type;
    private final WebSocketClient webSocket;
    private final Ratelimiter ratelimiter;
    private final List<Server> servers;
    private final DiscordUtils utils;
    private final Collection<ListenerManager<? extends DiscordAttachableListener>> listenerManangers = new ArrayList<>();
    private final int thisShard;
    private final int shardCount;
    private final Self self;
    private final Cache<Server, Long, Long> serverCache;
    private final Cache<User, Long, Long> userCache;
    private final Cache<Role, Long, IDPair> roleCache;
    private final Cache<Channel, Long, Long> channelCache;
    private final Cache<Message, Long, IDPair> messageCache;
    private final Cache<CustomEmoji, Long, IDPair> emojiCache;
    private CompletableFuture<Self> selfFuture;
    private boolean init = false;
    private LivingInt serversInit;

    public DiscordInternal(String token, AccountType type, int thisShard, int ShardCount) {
        this.serverCache = CoreDelegate.serverCache(this);
        this.userCache = CoreDelegate.userCache(this);
        this.roleCache = CoreDelegate.roleCache(this);
        this.channelCache = CoreDelegate.channelCache(this);
        this.messageCache = CoreDelegate.messageCache(this);
        this.emojiCache = CoreDelegate.emojiCache(this);
        selfFuture = new CompletableFuture<>();
        serversInit = new LivingInt(5, 0, -1, 1, TimeUnit.SECONDS);
        serversInit.onStopHit(() -> init = true);
        this.thisShard = thisShard;
        this.shardCount = ShardCount;
        this.pool = CoreDelegate.newInstance(ThreadPool.class, this);
        this.token = token;
        Logger.addBlankedWord(token);
        this.type = type;
        this.ratelimiter = CoreDelegate.newInstance(Ratelimiter.class, this);
        this.webSocket = CoreDelegate.newInstance(WebSocketClient.class, this);
        this.utils = new DiscordUtils(this);

        servers = new ArrayList<>();

        logger.info("Waiting for initialization to finish...");
        self = selfFuture.join();
        logger.info("Discord connection for user " + self.getDiscriminatedName() + " is ready!");
    }

    public DiscordInternal(String token) {
        this.pool = CoreDelegate.newInstance(ThreadPool.class, this, 1, "GetShards Discord Pool");
        this.token = token;
        this.type = AccountType.BOT;
        this.webSocket = null;
        this.ratelimiter = CoreDelegate.newInstance(Ratelimiter.class, this);
        this.servers = null;
        this.utils = null;
        this.thisShard = 0;
        this.shardCount = 1;
        this.self = null;
        this.serverCache = null;
        this.userCache = null;
        this.roleCache = null;
        this.channelCache = null;
        this.messageCache = null;
        this.emojiCache = null;
    }

    // Override Methods
    @Override
    public ThreadPool getThreadPool() {
        return pool;
    }

    @Override
    public Collection<Server> getServers() {
        return servers;
    }

    @Override
    public Collection<User> getUsers() {
        return null; // todo
    }

    @Override
    public int getServerCount() {
        return 0;
    }

    @Override
    public int getUserCount() {
        return 0;
    }

    @Override
    public Cache<Server, Long, Long> getServerCache() {
        return serverCache;
    }

    @Override
    public Cache<User, Long, Long> getUserCache() {
        return userCache;
    }

    @Override
    public Cache<Role, Long, IDPair> getRoleCache() {
        return roleCache;
    }

    @Override
    public Cache<Channel, Long, Long> getChannelCache() {
        return channelCache;
    }

    @Override
    public Cache<Message, Long, IDPair> getMessageCache() {
        return messageCache;
    }

    @Override
    public Cache<CustomEmoji, Long, IDPair> getEmojiCache() {
        return emojiCache;
    }

    public Collection<DiscordAttachableListener> getAttachedListeners() {
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
    public DiscordUtils getUtilities() {
        return utils;
    }

    @Override
    public Optional<Channel> getChannelById(long id) {
        return servers.stream()
                .flatMap(server -> server.getChannels()
                        .stream())
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
        return Evaluation.of(listenerManangers.removeIf(manager -> manager.getListener()
                .equals(listener)));
    }

    @Override
    public <C extends DiscordAttachableListener> ListenerManager<C> attachListener(C listener) {
        ListenerManagerInternal<C> manager = ListenerManagerInternal.getInstance(this, listener);
        listenerManangers.add(manager);
        return manager;
    }

    @Override
    public String toString() {
        return "Discord Connection to " + self;
    }

    public boolean initFinished() {
        return init;
    }

    public Collection<ListenerManager<? extends DiscordAttachableListener>> getListenerManagers() {
        return listenerManangers;
    }

    public WebSocketClient getWebSocket() {
        return webSocket;
    }

    public Ratelimiter getRatelimiter() {
        return ratelimiter;
    }

    public Collection<ListenerManager<? extends DiscordAttachableListener>> getAllListenerManagers() {
        return listenerManangers;
    }

    public CompletableFuture<Self> getSelfFuture() {
        return selfFuture;
    }

    public void addServer(Server server) {
        servers.add(server);
        serversInit.set(5);
    }
}

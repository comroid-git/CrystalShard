package de.kaleidox.crystalshard.internal;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.internal.core.concurrent.ThreadPool;
import de.kaleidox.crystalshard.internal.core.net.request.Ratelimiting;
import de.kaleidox.crystalshard.internal.core.net.socket.WebSocketClient;
import de.kaleidox.crystalshard.internal.items.server.ServerInternal;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.items.channel.Channel;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.items.user.AccountType;
import de.kaleidox.crystalshard.main.items.user.Self;
import de.kaleidox.crystalshard.main.items.user.User;
import de.kaleidox.crystalshard.main.listener.DiscordAttachableListener;
import de.kaleidox.crystalshard.main.listener.MessageCreateListener;
import de.kaleidox.crystalshard.main.listener.ServerCreateListener;
import de.kaleidox.crystalshard.util.DiscordUtils;
import de.kaleidox.logging.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executor;

public class DiscordInternal implements Discord {
    private final static Logger logger = new Logger(DiscordInternal.class);
    private final ThreadPool pool;
    private final String token;
    private final AccountType type;
    private final WebSocketClient webSocket;
    private final Ratelimiting ratelimiter;
    private final List<Server> servers;
    private final DiscordUtils utils;
    private final Collection<DiscordAttachableListener> listeners = new ArrayList<>();
    private Self self;

    public DiscordInternal(String token, AccountType type) {
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
            Thread.sleep(waitMs);
            logger.info("Discord connection for user " + self.getDiscriminatedName() + " is ready!");
        } catch (InterruptedException e) {
            logger.exception(e);
        }
    }

    public ThreadPool getThreadPool() {
        return pool;
    }

    @Override
    public void attachMessageCreateListener(MessageCreateListener listener) {
        listeners.add(listener);
    }

    public Collection<DiscordAttachableListener> getListeners() {
        return listeners;
    }

    @Override
    public String getPrefixedToken() {
        return type.getPrefix() + token;
    }

    @Override
    public int getShardId() {
        return 0;
    }

    @Override
    public int getShards() {
        return 1;
    }

    @Override
    public void addServerCreateListener(ServerCreateListener listener) {

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

    @Override
    public long getId() {
        return 0;
    }

    @Override
    public Discord getDiscord() {
        return null;
    }
}

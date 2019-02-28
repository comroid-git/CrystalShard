package de.kaleidox.crystalshard.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

import de.kaleidox.crystalshard.Log;
import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.api.entity.user.Self;
import de.kaleidox.crystalshard.api.handling.listener.DiscordAttachableListener;
import de.kaleidox.crystalshard.api.handling.listener.ListenerManager;
import de.kaleidox.crystalshard.core.concurrent.ThreadPool;
import de.kaleidox.crystalshard.core.concurrent.ThreadPoolImpl;
import de.kaleidox.crystalshard.core.listener.model.ListenerAttachableBase;
import de.kaleidox.crystalshard.core.net.request.HttpMethod;
import de.kaleidox.crystalshard.core.net.request.WebRequestImpl;
import de.kaleidox.crystalshard.core.net.request.endpoint.DiscordEndpoint;
import de.kaleidox.crystalshard.core.net.request.ratelimit.RatelimiterImpl;
import de.kaleidox.crystalshard.core.net.request.ratelimiting.Ratelimiter;
import de.kaleidox.crystalshard.core.net.socket.WebSocketClient;
import de.kaleidox.crystalshard.core.net.socket.WebSocketClientImpl;
import de.kaleidox.crystalshard.util.DiscordUtils;
import de.kaleidox.util.functional.Evaluation;

import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class DiscordImpl extends ListenerAttachableBase<DiscordAttachableListener> implements Discord {
    public static final Logger logger = Log.get(DiscordImpl.class);

    private final String token;
    private final int currentShard;
    private final ThreadPoolImpl threadpool;
    private final WebSocketClientImpl websocket;
    private final RatelimiterImpl ratelimiter;

    private final Self self;

    private List<DiscordImpl> shardsSorted;
    private DiscordUtils utils;

    public DiscordImpl(final String token, int currentShard) {
        //noinspection ConstantConditions
        super(null);
        super.discord = this;

        this.token = token;

        this.currentShard = currentShard;
        this.threadpool = new ThreadPoolImpl(this);
        this.websocket = new WebSocketClientImpl(this);
        this.ratelimiter = new RatelimiterImpl(this);

        this.self = websocket.getSelfFuture().join();
    }

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public ThreadPool getThreadPool() {
        return threadpool;
    }

    @Override
    public WebSocketClient getWebSocket() {
        return websocket;
    }

    @Override
    public Ratelimiter getRatelimiter() {
        return ratelimiter;
    }

    @Override
    public int shardID() {
        return currentShard;
    }

    @Override
    public int shardCount() {
        return shardsSorted.size();
    }

    @Override
    public Discord getShard(int shardNumber) {
        return shardsSorted.get(shardNumber);
    }

    @Override
    public Self getYourself() {
        return self;
    }

    @Override
    public DiscordUtils getUtilities() {
        if (utils != null) return utils;
        ServiceLoader.load(DiscordUtils.class).findFirst().ifPresent(u -> this.utils = u);
        return this.utils;
    }

    @Override
    public <C extends DiscordAttachableListener> ListenerManager<C> attachListener(C listener) {
        return null;
    }

    @Override
    public Evaluation<Boolean> detachListener(DiscordAttachableListener listener) {
        return null;
    }

    @Override
    public Collection<ListenerManager<? extends DiscordAttachableListener>> getListenerManagers(Predicate<ListenerManager<L>> filter) {
        return null;
    }

    @Override
    public Collection<DiscordAttachableListener> getListeners() {
        return null;
    }

    static class GroupedDiscord extends ArrayList<Discord> implements Discord {
        private final Collection<? extends Discord> shardsOrdered;

        public GroupedDiscord(@NotNull List<DiscordImpl> c) {
            super(c);
            this.shardsOrdered = c;

            c.forEach(shard -> shard.shardsSorted = c);
        }
    }

    public static class BuilderImpl implements Discord.Builder {
        private String token;

        @Override
        public Builder setToken(String token) {
            Log.PrivacyPrintStream.silence(token);
            this.token = token;
            return this;
        }

        @Override
        public CompletableFuture<Discord> login() {
            Objects.requireNonNull(token, "Token must be set first!");

            return new WebRequestImpl<Integer>()
                    .addHeader("Authentication", "Bot " + token)
                    .setMethod(HttpMethod.GET)
                    .setUri(DiscordEndpoint.GATEWAY_BOT.createUri())
                    .executeAs(node -> node.path("shards").asInt(1))
                    .thenApply(shardCount -> {
                        final List<DiscordImpl> shards = new ArrayList<>();
                        IntStream.range(0, shardCount)
                                .forEachOrdered(shard -> shards.add(new DiscordImpl(token, shard)));
                        return shards;
                    })
                    .thenApply(GroupedDiscord::new);
        }
    }
}

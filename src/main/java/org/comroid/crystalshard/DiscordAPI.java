package org.comroid.crystalshard;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.comroid.api.ContextualProvider;
import org.comroid.common.Disposable;
import org.comroid.crystalshard.entity.SnowflakeCache;
import org.comroid.crystalshard.rest.Endpoint;
import org.comroid.restless.HttpAdapter;
import org.comroid.restless.REST;
import org.comroid.restless.server.Ratelimiter;

import java.io.Closeable;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class DiscordAPI extends ContextualProvider.Base implements Context, Closeable {
    public static final Context CTX = ContextualProvider.getRoot().upgrade(Context.class);
    public static final String URL_BASE = "https://discord.com/api";
    public static final String CDN_URL_BASE = "https://cdn.discordapp.com/";
    public static final int GLOBAL_RATELIMIT = 50;
    private static final Logger logger = LogManager.getLogger();
    private final REST rest;
    private final SnowflakeCache snowflakeCache;
    private final ScheduledExecutorService scheduledExecutorService;

    public SnowflakeCache getSnowflakeCache() {
        return snowflakeCache;
    }

    public REST getREST() {
        return rest;
    }

    public DiscordAPI() {
        this(Executors.newScheduledThreadPool(4));
    }

    public DiscordAPI(ScheduledExecutorService scheduledExecutorService) {
        super(CTX);

        this.scheduledExecutorService = scheduledExecutorService;

        Ratelimiter ratelimiter = Ratelimiter.ofPool(scheduledExecutorService, Endpoint.values());
        this.rest = new REST(this, scheduledExecutorService, ratelimiter);

        this.snowflakeCache = new SnowflakeCache(this);

        addToContext(scheduledExecutorService, rest, snowflakeCache);
    }

    public static long getIdFromToken(String token) {
        String decode = new String(Base64.getDecoder().decode(token));
        StringBuilder num = new StringBuilder();
        for (int i = 0; i < decode.length(); i++) {
            char c = decode.charAt(i);
            if (Character.isDigit(c))
                num.append(c);
            else break;
        }
        return Long.parseLong(num.toString());
    }

    public static int getShardIdForGuild(Bot bot, long id) {
        return Math.toIntExact((id >> 22) % bot.getShardCount());
    }

    @Override
    public void close() throws Disposable.MultipleExceptions {
        logger.warn("Shutting down Discord API Object and contextual relatives!");

        List<Exception> exceptions = Stream.concat(
                streamContextMembers(true).filter(DiscordBotBase.class::isInstance),
                streamContextMembers(true).filter(it -> !(it instanceof DiscordBotBase))
        ).sequential()
                .filter(AutoCloseable.class::isInstance)
                .map(AutoCloseable.class::cast)
                .map(closeable -> {
                    try {
                        closeable.close();
                    } catch (Exception e) {
                        return e;
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        if (exceptions.size() > 0)
            throw new Disposable.MultipleExceptions("Exceptions occurred during shutdown", exceptions);
        scheduledExecutorService.shutdown();
    }
}

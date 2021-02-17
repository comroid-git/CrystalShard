package org.comroid.crystalshard;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.comroid.api.ContextualProvider;
import org.comroid.api.Polyfill;
import org.comroid.common.Disposable;
import org.comroid.crystalshard.entity.SnowflakeCache;
import org.comroid.crystalshard.rest.Endpoint;
import org.comroid.mutatio.span.Span;
import org.comroid.restless.HttpAdapter;
import org.comroid.restless.REST;
import org.comroid.restless.endpoint.CompleteEndpoint;
import org.comroid.restless.server.Ratelimiter;
import org.comroid.uniform.SerializationAdapter;
import org.comroid.uniform.node.UniNode;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.container.DataContainer;
import org.jetbrains.annotations.ApiStatus.Internal;

import java.io.Closeable;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class DiscordAPI extends ContextualProvider.Base implements Context, Closeable {
    public static final String URL_BASE = "https://discord.com/api";
    public static final String CDN_URL_BASE = "https://cdn.discordapp.com/";
    private static final Logger logger = LogManager.getLogger();
    public static SerializationAdapter SERIALIZATION = null;
    public final HttpAdapter httpAdapter;
    private final REST rest;
    private final SnowflakeCache snowflakeCache;
    private final ScheduledExecutorService scheduledExecutorService;

    public SnowflakeCache getSnowflakeCache() {
        return snowflakeCache;
    }

    public REST getREST() {
        return rest;
    }

    @Deprecated
    @Internal
    public static <T extends DataContainer<? super T>, R, N extends UniNode> CompletableFuture<R> newRequest(
            DiscordAPI context,
            String token,
            REST.Method method,
            CompleteEndpoint endpoint,
            N body,
            GroupBind<T> responseType,
            Function<Span<T>, R> spanResolver
    ) {
        REST.Request<T> req = responseType == null
                ? Polyfill.uncheckedCast(context.getREST().request())
                : context.getREST().request(responseType);
        req.endpoint(endpoint)
                .addHeaders(DiscordREST.createHeaders(token));
        if (body != null && method != REST.Method.GET)
            req.body(body.toString());
        return req.method(method)
                .execute$deserialize()
                .thenApply(spanResolver)
                .exceptionally(context.exceptionLogger(
                        logger,
                        Level.ERROR,
                        String.format("%s-Request @ %s", method, endpoint.getSpec()),
                        false
                ));
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

    public DiscordAPI(HttpAdapter httpAdapter) {
        this(httpAdapter, Executors.newScheduledThreadPool(4));
    }

    public DiscordAPI(HttpAdapter httpAdapter, ScheduledExecutorService scheduledExecutorService) {
        super((Base) SERIALIZATION, httpAdapter);

        Objects.requireNonNull(SERIALIZATION, "SERIALIZATION must be provided");

        this.scheduledExecutorService = scheduledExecutorService;
        this.httpAdapter = httpAdapter;

        Ratelimiter ratelimiter = Ratelimiter.ofPool(
                scheduledExecutorService,
                Endpoint.values()
        );
        this.rest = new REST(this, scheduledExecutorService, ratelimiter);

        this.snowflakeCache = new SnowflakeCache(this);

        addToContext(httpAdapter, scheduledExecutorService, rest, snowflakeCache);
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

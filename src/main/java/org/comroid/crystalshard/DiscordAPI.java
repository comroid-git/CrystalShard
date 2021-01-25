package org.comroid.crystalshard;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.comroid.api.ContextualProvider;
import org.comroid.api.Polyfill;
import org.comroid.crystalshard.entity.SnowflakeCache;
import org.comroid.crystalshard.rest.Endpoint;
import org.comroid.mutatio.span.Span;
import org.comroid.restless.CommonHeaderNames;
import org.comroid.restless.HttpAdapter;
import org.comroid.restless.REST;
import org.comroid.restless.endpoint.CompleteEndpoint;
import org.comroid.restless.server.Ratelimiter;
import org.comroid.uniform.SerializationAdapter;
import org.comroid.uniform.node.UniNode;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.container.DataContainer;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;

import java.util.Base64;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Function;

public final class DiscordAPI extends ContextualProvider.Base implements Context {
    public static final String URL_BASE = "https://discord.com/api";
    public static final String CDN_URL_BASE = "https://cdn.discordapp.com/";
    private static final Logger logger = LogManager.getLogger();
    public static SerializationAdapter SERIALIZATION = null;
    public final HttpAdapter httpAdapter;
    private final REST rest;
    private final SnowflakeCache snowflakeCache;
    private final ScheduledExecutorService scheduledExecutorService;
    final Span<Object> members;

    @Override
    public ContextualProvider getUnderlyingContextualProvider() {
        return this;
    }

    @Override
    public Span<Object> getContextMembers() {
        return members;
    }

    public SnowflakeCache getSnowflakeCache() {
        return snowflakeCache;
    }

    public REST getREST() {
        return rest;
    }

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
                .addHeaders(createHeaders(token));
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

    @NotNull
    @Internal
    public static REST.Header.List createHeaders(String token) {
        REST.Header.List headers = new REST.Header.List();

        if (token != null)
            headers.add(CommonHeaderNames.AUTHORIZATION, "Bot " + token);
        headers.add(CommonHeaderNames.USER_AGENT, String.format(
                "DiscordBot (%s, %s) %s",
                CrystalShard.URL,
                CrystalShard.VERSION.toSimpleString(),
                CrystalShard.VERSION.toString())
        );
        return headers;
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
        Objects.requireNonNull(SERIALIZATION, "SERIALIZATION must be provided");

        this.scheduledExecutorService = scheduledExecutorService;
        this.httpAdapter = httpAdapter;

        Ratelimiter ratelimiter = Ratelimiter.ofPool(
                scheduledExecutorService,
                Endpoint.values()
        );
        this.rest = new REST(this, scheduledExecutorService, ratelimiter);

        this.snowflakeCache = new SnowflakeCache(this);

        this.members = Span.make().initialValues(SERIALIZATION, httpAdapter, scheduledExecutorService, rest, snowflakeCache, this).span();
    }

}

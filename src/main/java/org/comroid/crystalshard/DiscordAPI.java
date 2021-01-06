package org.comroid.crystalshard;

import com.google.common.flogger.FluentLogger;
import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.rest.Endpoint;
import org.comroid.crystalshard.rest.response.AbstractRestResponse;
import org.comroid.mutatio.span.Span;
import org.comroid.restless.CommonHeaderNames;
import org.comroid.restless.HttpAdapter;
import org.comroid.restless.REST;
import org.comroid.restless.endpoint.RatelimitedEndpoint;
import org.comroid.restless.server.Ratelimiter;
import org.comroid.uniform.SerializationAdapter;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Function;
import java.util.logging.Level;

public final class DiscordAPI extends ContextualProvider.Base {
    public static final String URL_BASE = "https://discord.com/api";
    public static final String CDN_URL_BASE = "https://cdn.discordapp.com/";
    private static final FluentLogger LOGGER = FluentLogger.forEnclosingClass();
    public static SerializationAdapter SERIALIZATION = null;
    private final HttpAdapter httpAdapter;
    private final REST rest;
    private final SnowflakeCache snowflakeCache;
    private final ScheduledExecutorService scheduledExecutorService;
    private final Span<Object> members;

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

    @ApiStatus.Internal
    public static <R extends AbstractRestResponse> CompletableFuture<R> newRequest(
            DiscordAPI context,
            String token,
            REST.Method method,
            Endpoint<R> endpoint
    ) {
        return context.getREST()
                .request(endpoint)
                .addHeaders(createHeaders(token))
                .method(method)
                .execute$deserializeSingle()
                .exceptionally(context.exceptionLogger(LOGGER, Level.SEVERE, String.format("%s-Request @ %s", method, endpoint)));
    }

    @NotNull
    static REST.Header.List createHeaders(String token) {
        REST.Header.List headers = new REST.Header.List();

        headers.add(CommonHeaderNames.AUTHORIZATION, "Bot " + token);
        headers.add(CommonHeaderNames.USER_AGENT, String.format(
                "DiscordBot (%s, %s) %s",
                CrystalShard.URL,
                CrystalShard.VERSION.toSimpleString(),
                CrystalShard.VERSION.toString())
        );
        return headers;
    }

    public <T> Function<Throwable, T> exceptionLogger(final FluentLogger logger, final Level level, final String message) {
        return throwable -> {
            logger.at(level).withCause(throwable).log(message);
            return null;
        };
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
                Endpoint.values.toArray(new RatelimitedEndpoint[0])
        );
        this.rest = new REST(this, scheduledExecutorService, ratelimiter);

        this.snowflakeCache = new SnowflakeCache(this);

        this.members = Span.immutable(SERIALIZATION, httpAdapter, scheduledExecutorService, snowflakeCache);
    }
}

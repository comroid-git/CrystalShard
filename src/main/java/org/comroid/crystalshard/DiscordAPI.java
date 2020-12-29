package org.comroid.crystalshard;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.rest.Endpoint;
import org.comroid.mutatio.span.Span;
import org.comroid.restless.HttpAdapter;
import org.comroid.restless.REST;
import org.comroid.restless.endpoint.RatelimitedEndpoint;
import org.comroid.restless.server.Ratelimiter;
import org.comroid.uniform.SerializationAdapter;

import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public final class DiscordAPI implements ContextualProvider {
    public static final String URL_BASE = "https://discord.com/api";
    public static final String CDN_URL_BASE = "https://cdn.discordapp.com/";
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

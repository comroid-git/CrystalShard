package org.comroid.crystalshard;

import org.comroid.api.ContextualProvider;
import org.comroid.mutatio.span.Span;
import org.comroid.restless.HttpAdapter;
import org.comroid.uniform.SerializationAdapter;

import java.util.Objects;
import java.util.concurrent.ScheduledExecutorService;

public final class DiscordAPI implements ContextualProvider {
    public static final String URL_BASE = "https://discord.com/api";
    public static SerializationAdapter SERIALIZATION = null;
    private final HttpAdapter httpAdapter;
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

    public DiscordAPI(HttpAdapter httpAdapter) {
        this(httpAdapter, null);
    }

    public DiscordAPI(HttpAdapter httpAdapter, ScheduledExecutorService scheduledExecutorService) {
        Objects.requireNonNull(SERIALIZATION, "SERIALIZATION must be provided");

        this.scheduledExecutorService = scheduledExecutorService;
        this.httpAdapter = httpAdapter;

        this.snowflakeCache = new SnowflakeCache(this);

        this.members = Span.immutable(SERIALIZATION, httpAdapter, scheduledExecutorService, snowflakeCache);
    }
}

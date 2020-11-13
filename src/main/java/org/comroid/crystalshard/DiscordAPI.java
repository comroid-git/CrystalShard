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
    private final ScheduledExecutorService scheduledExecutorService;

    @Override
    public Span<Object> getContextMembers() {
        return Span.immutable(SERIALIZATION, httpAdapter, scheduledExecutorService);
    }

    public DiscordAPI(HttpAdapter httpAdapter) {
        this(httpAdapter, null);
    }

    public DiscordAPI(HttpAdapter httpAdapter, ScheduledExecutorService scheduledExecutorService) {
        Objects.requireNonNull(SERIALIZATION, "SERIALIZATION must be provided");

        this.scheduledExecutorService = scheduledExecutorService;
        this.httpAdapter = httpAdapter;
    }
}

package org.comroid.crystalshard;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.gateway.Gateway;
import org.comroid.crystalshard.gateway.event.GatewayEvent;
import org.comroid.crystalshard.gateway.event.HelloEvent;
import org.comroid.crystalshard.rest.Endpoint;
import org.comroid.crystalshard.rest.response.AbstractRestResponse;
import org.comroid.mutatio.pipe.Pipe;
import org.comroid.mutatio.ref.FutureReference;
import org.comroid.restless.CommonHeaderNames;
import org.comroid.restless.HttpAdapter;
import org.comroid.restless.REST;
import org.comroid.restless.endpoint.RatelimitedEndpoint;
import org.comroid.restless.server.Ratelimiter;
import org.comroid.restless.socket.WebsocketPacket;
import org.comroid.uniform.SerializationAdapter;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public abstract class DiscordBot implements ContextualProvider.Underlying, Closeable {
    public final FutureReference<? extends Gateway> gateway;
    private final DiscordAPI context;
    private final String token;
    private final SnowflakeCache snowflakeCache;
    private final REST rest;
    private final List<Consumer<DiscordBot>> readyTasks = new ArrayList<>();

    public Pipe<? extends WebsocketPacket> getPacketPipeline() {
        return gateway.into(Gateway::getPacketPipeline);
    }

    public Pipe<? extends GatewayEvent> getEventPipeline() {
        return gateway.into(Gateway::getEventPipeline);
    }

    @Override
    public ContextualProvider getUnderlyingContextualProvider() {
        return context.plus(this);
    }

    public boolean isReady() {
        return gateway.future.isDone();
    }

    protected abstract String getToken();

    protected DiscordBot(DiscordAPI context) {
        this.context = context;
        this.token = "Bot " + getToken();

        this.snowflakeCache = new SnowflakeCache(context);

        HttpAdapter httpAdapter = requireFromContext(HttpAdapter.class);
        SerializationAdapter serializationAdapter = requireFromContext(SerializationAdapter.class);
        ScheduledExecutorService executor = getFromContext(ScheduledExecutorService.class)
                .orElseGet(() -> Executors.newScheduledThreadPool(4));
        Ratelimiter ratelimiter = Ratelimiter.ofPool(executor, Endpoint.values.toArray(new RatelimitedEndpoint[0]));

        this.rest = new REST(context, executor, ratelimiter);
        this.gateway = new FutureReference<>(
                newRequest(REST.Method.GET, Endpoint.GATEWAY_BOT)
                        .thenCompose(gbr -> httpAdapter.createWebSocket(executor, gbr.uri.get(), createHeaders()))
                        .thenApply(socket -> new Gateway(getUnderlyingContextualProvider(), socket))
                        .thenCompose(gateway -> gateway.getEventPipeline()
                                .flatMap(HelloEvent.class)
                                .next()
                                .thenApply(hello -> {
                                    hello.heartbeatInterval.consume(this::startHeartbeat);
                                    return gateway;
                                })));
        gateway.future.thenRun(() -> readyTasks.forEach(task -> task.accept(this)));

        whenReady(bot -> {
            bot.getEventPipeline();
        });
    }

    @Internal
    public <R extends AbstractRestResponse> CompletableFuture<R> newRequest(
            REST.Method method,
            Endpoint<R> endpoint
    ) {
        return rest.request(endpoint)
                .addHeaders(createHeaders())
                .method(method)
                .execute$deserializeSingle();
    }

    protected void whenReady(Consumer<DiscordBot> readyTask) {
        readyTasks.add(readyTask);
    }

    @Override
    public void close() throws IOException {
        gateway.future.join().close();
    }

    @NotNull
    private REST.Header.List createHeaders() {
        REST.Header.List headers = new REST.Header.List();

        headers.add(CommonHeaderNames.AUTHORIZATION, token);
        headers.add(CommonHeaderNames.USER_AGENT, String.format(
                "DiscordBot (%s, %s) %s",
                CrystalShard.URL,
                CrystalShard.VERSION.toSimpleString(),
                CrystalShard.VERSION.toString())
        );
        return headers;
    }

    private void startHeartbeat(int interval) {
        requireFromContext(ScheduledExecutorService.class)
                .scheduleAtFixedRate(() -> gateway.consume(Gateway::sendHeartbeat), interval, interval, TimeUnit.MILLISECONDS);
    }
}

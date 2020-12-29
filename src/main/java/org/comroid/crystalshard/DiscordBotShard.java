package org.comroid.crystalshard;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.entity.user.User;
import org.comroid.crystalshard.gateway.Gateway;
import org.comroid.crystalshard.gateway.event.GatewayEvent;
import org.comroid.crystalshard.gateway.event.generic.HelloEvent;
import org.comroid.crystalshard.rest.Endpoint;
import org.comroid.crystalshard.rest.response.AbstractRestResponse;
import org.comroid.mutatio.pipe.Pipe;
import org.comroid.mutatio.ref.FutureReference;
import org.comroid.restless.CommonHeaderNames;
import org.comroid.restless.HttpAdapter;
import org.comroid.restless.REST;
import org.comroid.restless.socket.WebsocketPacket;
import org.comroid.uniform.SerializationAdapter;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public final class DiscordBotShard implements Bot {
    private final DiscordAPI context;
    private final String token;
    private final FutureReference<? extends Gateway> gateway;
    private final List<Consumer<DiscordBotShard>> readyTasks = new ArrayList<>();

    @Override
    public SnowflakeCache getSnowflakeCache() {
        return context.getSnowflakeCache();
    }

    public Pipe<? extends WebsocketPacket> getPacketPipeline() {
        return gateway.into(Gateway::getPacketPipeline);
    }

    @Override
    public Pipe<? extends GatewayEvent> getEventPipeline() {
        return gateway.into(Gateway::getEventPipeline);
    }

    @Override
    public ContextualProvider getUnderlyingContextualProvider() {
        return context.plus(this);
    }

    @Override
    public boolean isReady() {
        return gateway.future.isDone() && getGateway().readyEvent.future.isDone();
    }

    public Gateway getGateway() {
        return gateway.assertion();
    }

    @Override
    public User getYourself() {
        return gateway.flatMap(gateway -> gateway.readyEvent)
                .flatMap(readyEvent -> readyEvent.yourself)
                .assertion();
    }

    @Override
    public int getCurrentShardID() {
        return gateway.flatMap(gateway -> gateway.readyEvent)
                .flatMap(readyEvent -> readyEvent.shard)
                .map(array -> array.get(0).asInt())
                .assertion();
    }

    @Override
    public int getShardCount() {
        return gateway.flatMap(gateway -> gateway.readyEvent)
                .flatMap(readyEvent -> readyEvent.shard)
                .map(array -> array.get(1).asInt())
                .assertion();
    }

    public DiscordBotShard(DiscordAPI context, String token, int shardID) {
        this.context = context;
        this.token = "Bot " + token;

        HttpAdapter httpAdapter = requireFromContext(HttpAdapter.class);
        SerializationAdapter serializationAdapter = requireFromContext(SerializationAdapter.class);
        ScheduledExecutorService executor = requireFromContext(ScheduledExecutorService.class);
        this.gateway = new FutureReference<>(
                newRequest(REST.Method.GET, Endpoint.GATEWAY_BOT)
                        .thenCompose(gbr -> httpAdapter.createWebSocket(executor, gbr.uri.get(), createHeaders(token)))
                        .thenApply(socket -> new Gateway(getUnderlyingContextualProvider(), socket))
                        .thenCompose(gateway -> gateway.getEventPipeline()
                                .flatMap(HelloEvent.class)
                                .next()
                                .thenApply(hello -> {
                                    hello.heartbeatInterval.consume(this::startHeartbeat);
                                    return gateway;
                                })));
        gateway.future
                .thenAccept(gateway -> gateway.sendIdentify(shardID).join())
                .thenRun(() -> readyTasks.forEach(task -> task.accept(this)));
    }

    @Override
    public void close() throws IOException {
        gateway.future.join().close();
    }

    @Override
    @Internal
    public <R extends AbstractRestResponse> CompletableFuture<R> newRequest(
            REST.Method method,
            Endpoint<R> endpoint
    ) {
        return context.getREST()
                .request(endpoint)
                .addHeaders(createHeaders(token))
                .method(method)
                .execute$deserializeSingle();
    }

    protected void whenReady(Consumer<DiscordBotShard> readyTask) {
        if (isReady())
            readyTask.accept(this);
        readyTasks.add(readyTask);
    }

    @NotNull
    static REST.Header.List createHeaders(String token) {
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
        final Gateway gateway = getGateway();

        gateway.heartbeatTime.set(interval);

        requireFromContext(ScheduledExecutorService.class).scheduleAtFixedRate(
                () -> gateway.sendHeartbeat().join(),
                interval,
                interval,
                TimeUnit.MILLISECONDS);
    }
}

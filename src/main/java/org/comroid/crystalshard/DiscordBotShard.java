package org.comroid.crystalshard;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.entity.user.User;
import org.comroid.crystalshard.gateway.Gateway;
import org.comroid.crystalshard.gateway.event.GatewayEvent;
import org.comroid.crystalshard.gateway.event.generic.HelloEvent;
import org.comroid.crystalshard.rest.Endpoint;
import org.comroid.crystalshard.rest.response.AbstractRestResponse;
import org.comroid.mutatio.pipe.Pipe;
import org.comroid.mutatio.ref.FutureReference;
import org.comroid.restless.HttpAdapter;
import org.comroid.restless.REST;
import org.comroid.restless.socket.WebsocketPacket;
import org.comroid.uniform.SerializationAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public final class DiscordBotShard implements Bot {
    private static final Logger logger = LogManager.getLogger();
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
                .map(array -> array.get(0))
                .assertion();
    }

    @Override
    public int getShardCount() {
        return gateway.flatMap(gateway -> gateway.readyEvent)
                .flatMap(readyEvent -> readyEvent.shard)
                .map(array -> array.get(1))
                .assertion();
    }

    @Override
    public <R extends AbstractRestResponse> CompletableFuture<R> newRequest(REST.Method method, Endpoint<R> endpoint) {
        return DiscordAPI.newRequest(context, token, method, endpoint);
    }

    public DiscordBotShard(DiscordAPI context, String token, int shardID) {
        this.context = context;
        this.token = "Bot " + token;

        HttpAdapter httpAdapter = requireFromContext(HttpAdapter.class);
        SerializationAdapter serializationAdapter = requireFromContext(SerializationAdapter.class);
        ScheduledExecutorService executor = requireFromContext(ScheduledExecutorService.class);
        this.gateway = new FutureReference<>(
                DiscordAPI.newRequest(context, token, REST.Method.GET, Endpoint.GATEWAY_BOT)
                        .thenCompose(gbr -> httpAdapter.createWebSocket(executor, gbr.uri.get(), DiscordAPI.createHeaders(token)))
                        .thenApply(socket -> new Gateway(context, socket))
                        .thenCompose(gateway -> gateway.getEventPipeline()
                                .peek(it -> logger.debug("DEBUG 1 - {} - {}", it.getClass().getSimpleName(), it)) // todo
                                .flatMap(HelloEvent.class)
                                .peek(it -> logger.debug("DEBUG 2 - {} - {}", it.getClass().getSimpleName(), it)) // todo
                                .next()
                                .thenApply(hello -> {
                                    logger.debug("DEBUG 3 - {} - {}", hello.getClass().getSimpleName(), hello);
                                    hello.heartbeatInterval.consume(this::startHeartbeat);
                                    return gateway;
                                }))
                        .exceptionally(context.exceptionLogger(logger, Level.FATAL, "Could not create Gateway")));
        gateway.future
                .thenAccept(gateway -> gateway.sendIdentify(shardID).join())
                .thenRun(() -> {
                    readyTasks.forEach(task -> task.accept(this));
                    logger.info(String.format("%s - Shard %d is ready!", toString(), getCurrentShardID()));
                })
                .exceptionally(context.exceptionLogger(logger, Level.FATAL, "Could not send Identify"));
    }

    @Override
    public void close() throws IOException {
        gateway.future.join().close();
    }

    protected void whenReady(Consumer<DiscordBotShard> readyTask) {
        if (isReady())
            readyTask.accept(this);
        readyTasks.add(readyTask);
    }

    private void startHeartbeat(int interval) {
        logger.debug(String.format("%s - Started heartbeating at interval: %d", toString(), interval));

        final Gateway gateway = getGateway();

        gateway.heartbeatTime.set(interval);

        requireFromContext(ScheduledExecutorService.class).scheduleAtFixedRate(
                () -> gateway.sendHeartbeat().join(),
                interval,
                interval,
                TimeUnit.MILLISECONDS);
    }

    @Override
    public String toString() {
        return String.format("DiscordBotShard<%s - Shard %d / %d>", getYourself(), getCurrentShardID(), getShardCount());
    }
}

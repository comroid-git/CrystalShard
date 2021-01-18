package org.comroid.crystalshard;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.entity.SnowflakeCache;
import org.comroid.crystalshard.entity.user.User;
import org.comroid.crystalshard.gateway.Gateway;
import org.comroid.crystalshard.gateway.GatewayIntent;
import org.comroid.crystalshard.gateway.event.GatewayEvent;
import org.comroid.mutatio.pipe.Pipe;
import org.comroid.mutatio.ref.FutureReference;
import org.comroid.restless.HttpAdapter;
import org.comroid.restless.REST;
import org.comroid.restless.body.BodyBuilderType;
import org.comroid.restless.endpoint.CompleteEndpoint;
import org.comroid.restless.socket.WebsocketPacket;
import org.comroid.uniform.SerializationAdapter;
import org.comroid.uniform.node.UniNode;
import org.comroid.util.Bitmask;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.container.DataContainer;
import org.jetbrains.annotations.ApiStatus.Internal;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;

public final class DiscordBotShard implements Bot {
    private static final Logger logger = LogManager.getLogger();
    public final DiscordAPI context;
    private final String token;
    private final int currentShardID;
    private final int shardCount;
    private final FutureReference<Gateway> gateway;
    @Internal
    public final List<Consumer<DiscordBotShard>> readyTasks = new ArrayList<>();

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
        return currentShardID;
    }

    @Override
    public int getShardCount() {
        return shardCount;
    }

    @Override
    public <R extends DataContainer<? super R>, N extends UniNode> CompletableFuture<R> newRequest(
            REST.Method method,
            CompleteEndpoint endpoint,
            GroupBind<R> responseType,
            BodyBuilderType<N> type,
            Consumer<N> builder
    ) {
        return DiscordAPI.newRequest(context, token, method, endpoint, responseType, type, builder);
    }

    @Override
    public String getToken() {
        return token;
    }

    public DiscordBotShard(DiscordAPI context, String token, URI wsUri, int shardID, int shardCount, GatewayIntent... intents) {
        context.members.add(this);
        this.context = context;
        this.token = token;
        this.currentShardID = shardID;
        this.shardCount = shardCount;

        HttpAdapter httpAdapter = requireFromContext(HttpAdapter.class);
        SerializationAdapter serializationAdapter = requireFromContext(SerializationAdapter.class);
        ScheduledExecutorService executor = requireFromContext(ScheduledExecutorService.class);
        this.gateway = new FutureReference<>(initiateGateway(token, wsUri, httpAdapter, executor, intents));

        /* todo: this is shit
        // restart on closed
        gateway.future.thenAccept(gateway -> gateway.getPacketPipeline()
                .filter(packet -> packet.getType() == WebsocketPacket.Type.CLOSE)
                .map(closed -> gateway)
                .peek(ThrowingConsumer.handling(Gateway::close, RuntimeException::new))
                .map(closed -> initiateGateway(token, wsUri, httpAdapter, executor, intents))
                .flatMap(FutureReference::new)
                .forEach(this.gateway::set));
         */
        gateway.into(gtw -> gtw.readyEvent).future
                .thenRun(() -> {
                    readyTasks.forEach(task -> task.accept(this));
                    logger.info(String.format("%s - Shard %d is ready!", toString(), getCurrentShardID()));
                });
    }

    private CompletableFuture<Gateway> initiateGateway(String token, URI wsUri, HttpAdapter httpAdapter, ScheduledExecutorService executor, GatewayIntent[] intents) {
        return httpAdapter.createWebSocket(executor, wsUri, DiscordAPI.createHeaders(token))
                .thenApply(socket -> new Gateway(this, socket, Bitmask.combine(intents)));
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

    @Override
    public String toString() {
        return String.format("DiscordBotShard<Shard %d / %d>", getCurrentShardID(), getShardCount());
    }
}

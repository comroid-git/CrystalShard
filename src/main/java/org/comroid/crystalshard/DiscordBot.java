package org.comroid.crystalshard;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.rest.Endpoint;
import org.comroid.crystalshard.rest.response.GatewayBotResponse;
import org.comroid.mutatio.ref.FutureReference;
import org.comroid.restless.CommonHeaderNames;
import org.comroid.restless.HttpAdapter;
import org.comroid.restless.REST;
import org.comroid.restless.server.Ratelimiter;
import org.comroid.restless.socket.Websocket;
import org.comroid.restless.socket.WebSocketPacket;
import org.comroid.uniform.SerializationAdapter;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;

public abstract class DiscordBot implements ContextualProvider.Underlying, Closeable {
    public final FutureReference<? extends Websocket> gateway;
    private final DiscordAPI context;
    private final String token;
    private final REST rest;
    private final List<Consumer<DiscordBot>> readyTasks = new ArrayList<>();

    @Override
    public ContextualProvider getUnderlyingContextualProvider() {
        return context.plus(this);
    }

    public boolean isReady() {
        return gateway.future.isDone();
    }

    protected abstract String getToken();

    protected void whenReady(Consumer<DiscordBot> readyTask) {
        readyTasks.add(readyTask);
    }

    @Override
    public void close() throws IOException {
        gateway.future.join().close();
    }

    protected DiscordBot(DiscordAPI context) {
        this.context = context;
        this.token = "Bot " + getToken();

        HttpAdapter httpAdapter = requireFromContext(HttpAdapter.class);
        SerializationAdapter serializationAdapter = requireFromContext(SerializationAdapter.class);
        ScheduledExecutorService executor = getFromContext(ScheduledExecutorService.class)
                .orElseGet(() -> Executors.newScheduledThreadPool(4));
        Ratelimiter ratelimiter = Ratelimiter.ofPool(executor, Endpoint.values());

        this.rest = new REST(context, executor, ratelimiter);
        this.gateway = new FutureReference<>(rest.request(GatewayBotResponse.TYPE)
                .endpoint(Endpoint.GATEWAY_BOT)
                .method(REST.Method.GET)
                .addHeader(CommonHeaderNames.AUTHORIZATION, token)
                .execute$deserializeSingle()
                .thenComposeAsync(gbr -> {
                    REST.Header.List headers = new REST.Header.List();

                    headers.add(CommonHeaderNames.AUTHORIZATION, token);
                    headers.add(CommonHeaderNames.USER_AGENT, String.format(
                            "DiscordBot (%s, %s) %s",
                            CrystalShard.URL,
                            CrystalShard.VERSION.toSimpleString(),
                            CrystalShard.VERSION.toString())
                    );

                    return httpAdapter.createWebSocket(executor, gbr.uri.get(), headers);
                }, executor)
                .thenCompose(socket -> socket.getPacketPipeline()
                        .filter(packet -> packet.getType() == WebSocketPacket.Type.DATA)
                        .map(p -> p.getData().into(serializationAdapter::parse))
                        .filter(data -> data.get("op").asInt() == 10)
                        .peek(data -> startHeartbeat(data.get("d").get("heartbeat_interval").asInt()))
                        .next()
                        .thenApply(data -> socket))
        );
    }

    private void startHeartbeat(int interval) {
        // todo
    }
}

package org.comroid.crystalshard.core.gateway;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;

import org.comroid.crystalshard.CrystalShard;
import org.comroid.crystalshard.abstraction.handling.AbstractEventHandler;
import org.comroid.crystalshard.adapter.Adapter;
import org.comroid.crystalshard.api.Discord;
import org.comroid.crystalshard.core.concurrent.ThreadPool;
import org.comroid.crystalshard.core.gateway.event.GatewayEventBase;
import org.comroid.crystalshard.core.gateway.event.HELLO;
import org.comroid.crystalshard.core.rest.DiscordEndpoint;
import org.comroid.crystalshard.core.rest.RestMethod;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.google.common.flogger.FluentLogger;

import static org.comroid.crystalshard.CrystalShard.URL;
import static org.comroid.crystalshard.CrystalShard.VERSION;

public class GatewayImpl extends AbstractEventHandler<GatewayEventBase> implements Gateway {
    private static final FluentLogger log = FluentLogger.forEnclosingClass();

    private final Discord api;
    public final CompletableFuture<Void> helloFuture;
    private final HttpClient client;
    private final SocketListener socketListener;
    private final WebSocket socket;
    private final ThreadPool threadPool;

    public GatewayImpl(Discord api, ThreadPool threadPool) {
        this.api = api;
        this.threadPool = threadPool;

        // prepare receiving HELLO
        helloFuture = listenTo(HELLO.class)
                .onlyOnce()
                .thenAccept(pair -> {
                    final int interval = pair.getHeartbeatInterval();

                    threadPool.scheduleAtFixedRate(() -> this.sendRequest(OpCode.HEARTBEAT, JSON.parseObject("{}")),
                                    interval, interval, TimeUnit.MILLISECONDS);
                });

        try {
            client = HttpClient.newBuilder()
                    .build();
            socketListener = new SocketListener();
            socket = client.newWebSocketBuilder()
                    .header("Authorization", "Bot " + api.getToken())
                    .header("User-Agent", "DiscordBot (" + URL + ", " + VERSION + ") using CrystalShard")
                    .buildAsync(new URI(CrystalShard.GATEWAY_DEFAULT_URL), socketListener)
                    // in case the stored URI does not work, request a new one
                    .exceptionally(throwable -> client.newWebSocketBuilder()
                            .header("Authorization", "Bot " + api.getToken())
                            .header("User-Agent", "DiscordBot (" + URL + ", " + VERSION + ") using CrystalShard")
                            .buildAsync(Adapter.<String>request(api)
                                    .endpoint(DiscordEndpoint.GATEWAY)
                                    .method(RestMethod.GET)
                                    .executeAsObject(node -> node.getString("url"))
                                    .thenApply(str -> {
                                        try {
                                            return new URI(str);
                                        } catch (URISyntaxException e) {
                                            throw new AssertionError("Unexpected URISyntaxException", e);
                                        }
                                    })
                                    .join(), socketListener)
                            .join())
                    /*
                    ok what the join, but nor is any exception expected on any of
                    these joins, nor should the exceptionally block ever run
                    */
                    .join();
        } catch (URISyntaxException e) {
            throw new RuntimeException("Initialization Exception", e);
        }
    }

    @Override
    public Discord getAPI() {
        return api;
    }

    @Override
    public CompletableFuture<Void> sendRequest(OpCode code, JSONObject payload) {
        return CompletableFuture.supplyAsync(() -> {
            JSONObject json = new JSONObject();

            json.put("op", code.value);
            json.put("d", payload);

            return socket.sendText(json.toString(), true);
        }, threadPool).thenApply(nil -> null);
    }

    class SocketListener implements WebSocket.Listener {
        final AtomicReference<StringBuilder> strb = new AtomicReference<>(new StringBuilder());

        @Override
        public void onOpen(WebSocket webSocket) {
            log.at(Level.INFO).log("WebSocket opened!");

            webSocket.request(1);
        }

        @Override
        public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
            log.at(Level.FINEST).log("Received textual data: " + data);

            synchronized (strb) {
                strb.get().append(data);
            }

            if (last) {
                api.getGatewayThreadPool()
                        .submit(new Runnable() {
                            final String rcv;
                            
                            {
                                synchronized (strb) {
                                    rcv = strb.get().toString();
                                    strb.set(new StringBuilder());
                                }
                            }
                            
                            @Override 
                            public void run() {
                                log.at(Level.FINER).log("Dispatching data: [ %s ]", rcv);
                                
                                final JSONObject json = JSON.parseObject(rcv);

                                threadPool.submit(() -> {
                                    String eventType = null;

                                    try {
                                        OpCode op = OpCode.getByValue(json.getInteger("op"));
                                        final JSONObject data = json.getJSONObject("d");
                                        int seq = json.getInteger("s");
                                        eventType = json.getString("t");

                                        final Class<?> eventClass = Class.forName("org.comroid.crystalshard.core.gateway.event." + eventType);

                                        submitEvent(Adapter.require(eventClass, api, data));
                                    } catch (JSONException e) {
                                        log.at(Level.SEVERE).withCause(e).log("JSON Deserialization Exception");
                                    } catch (ClassNotFoundException e) {
                                        log.at(CrystalShard.LogLevel.SKIPPED).log("Unrecognized Gateway Event Type: %s", eventType);
                                    }
                                });
                            }
                        });
            }

            webSocket.request(1);
            return null;
        }

        @Override
        public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
            log.at(Level.INFO).log("WebSocket closed with status " + GatewayStatusCodes.toString(statusCode));

            return null;
        }

        @Override
        public void onError(WebSocket webSocket, Throwable error) {
            log.at(Level.SEVERE).withCause(error).log("WebSocket encountered an Error");
        }
    }
}

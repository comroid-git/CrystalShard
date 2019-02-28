package de.kaleidox.crystalshard.core.net.socket;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicLong;

import de.kaleidox.crystalshard.CrystalShard;
import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.Log;
import de.kaleidox.crystalshard.api.entity.user.Self;
import de.kaleidox.crystalshard.core.concurrent.ThreadPoolImpl;
import de.kaleidox.crystalshard.core.net.request.DiscordRequestImpl;
import de.kaleidox.crystalshard.core.net.request.HttpMethod;
import de.kaleidox.crystalshard.core.net.request.endpoint.DiscordEndpoint;
import de.kaleidox.util.helpers.FutureHelper;
import de.kaleidox.util.helpers.JsonHelper;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.logging.log4j.Logger;

public class WebSocketClientImpl implements WebSocketClient {
    private static final Logger logger = Log.get(WebSocketClient.class);

    private final ThreadPoolImpl threadPool;
    private final Discord discord;
    private final Dispatch dispatch;
    private final WebSocket webSocket;
    private final AtomicLong lastPacket = new AtomicLong(0);
    private final AtomicLong lastHeartbeat = new AtomicLong(0);

    public WebSocketClientImpl(Discord discordObject) {
        URI gatewayUrl = new DiscordRequestImpl<String>(discordObject).setMethod(HttpMethod.GET)
                .setUri(DiscordEndpoint.GATEWAY.createUri())
                .executeAs(node -> node.get("url").asText())
                .exceptionally(throwable -> {
                    logger.error(throwable);
                    return "wss://gateway.discord.gg"; // default gateway if gateway couldn't be retrieved
                })
                .thenApply(URI::create)
                .join();
        this.threadPool = new ThreadPoolImpl(discordObject, 1, "WebSocketClient");
        this.discord = discordObject;
        this.dispatch = new Dispatch(discord, this);
        this.webSocket = HttpClient.newHttpClient()
                .newWebSocketBuilder()
                .header("Authorization", discordObject.getToken())
                .buildAsync(gatewayUrl, new WebSocketListener(discordObject))
                .join();
        identification();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> webSocket.sendClose(1000, "Shutting down!")));
    }

    private void identification() {
        ObjectNode data = JsonHelper.objectNode("properties",
                JsonHelper.objectNode(
                        "$os", JsonHelper.nodeOf(System.getProperty("os.name")),
                        "$browser", JsonHelper.nodeOf(CrystalShard.SHORT_FOOTPRINT),
                        "$device", JsonHelper.nodeOf(CrystalShard.SHORT_FOOTPRINT)));
        sendPayload(Payload.create(OpCode.IDENTIFY, data)).exceptionally(Log.exceptionLogger());
    }

    public CompletableFuture<Void> sendPayload(Payload payload) {
        assert payload != null : "Payload must not be null!";
        CompletableFuture<WebSocket> future = new CompletableFuture<>();
        this.threadPool.execute(() -> {
            while (lastPacket.get() > (System.currentTimeMillis() - 600)) {
                try {
                    Thread.sleep(50L);
                } catch (InterruptedException e) {
                    logger.error(e);
                }
            }
            if (lastPacket.get() <= (System.currentTimeMillis() - 500)) {
                logger.trace("Sending Packet with OpCode " + payload.getCode() + " and body: " + payload.getBody());
                CompletableFuture<WebSocket> subFut;
                boolean attachedFuture = false;
                for (Payload that : payload.split()) {
                    boolean last = that.isLast();
                    that.addNode("token", JsonHelper.nodeOf(discord.getToken()));
                    that.addNode("large_threshold", JsonHelper.nodeOf(250));
                    that.addNode("shard", JsonHelper.arrayNode(discord.shardID(), discord.shardCount()));
                    CharSequence nodeAsText = that.getSendableNode();
                    subFut = webSocket.sendText(nodeAsText, last);
                    if (last) lastPacket.set(System.currentTimeMillis());
                    if (!attachedFuture) {
                        FutureHelper.linkFutures(subFut, future);
                        attachedFuture = true;
                    }
                }
            }
        });
        return future.thenApply(n -> null);
    }

    public void heartbeat() {
        Payload payload = Payload.create(OpCode.HEARTBEAT, JsonHelper.nodeOf(null));
        sendPayload(payload);
        lastHeartbeat.set(System.currentTimeMillis());
    }

    public boolean respondToHeartbeat() {
        return lastHeartbeat.get() < System.currentTimeMillis() - 4000;
    }

    public Dispatch getDispatch() {
        return dispatch;
    }

    public CompletableFuture<Self> getSelfFuture() {
        return dispatch.selfFuture;
    }
}
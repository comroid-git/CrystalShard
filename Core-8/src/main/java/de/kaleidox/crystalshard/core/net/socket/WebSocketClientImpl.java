package de.kaleidox.crystalshard.core.net.socket;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.kaleidox.crystalshard.core.concurrent.ThreadPoolImpl;
import de.kaleidox.crystalshard.core.net.request.DiscordRequestImpl;
import de.kaleidox.crystalshard.core.net.request.HttpMethod;
import de.kaleidox.crystalshard.core.net.request.endpoint.DiscordEndpoint;
import de.kaleidox.crystalshard.logging.Logger;
import de.kaleidox.crystalshard.main.CrystalShard;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.util.helpers.JsonHelper;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicLong;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;

public class WebSocketClientImpl implements WebSocketClient {
    private static final Logger logger = new Logger(WebSocketClient.class);
    private static final OkHttpClient CLIENT = new OkHttpClient();
    private final Discord discord;
    private final WebSocket webSocket;
    private final AtomicLong lastPacket = new AtomicLong(0);
    private final AtomicLong lastHeartbeat = new AtomicLong(0);
    private final ThreadPoolImpl threadPool;

    public WebSocketClientImpl(Discord discordObject) {
        logger.warn("The Java 8 WebSocket implementation has not been tested yet."); // TODO: 31.10.2018 Test
        URL gatewayUrl = new DiscordRequestImpl<String>(discordObject).setMethod(HttpMethod.GET)
                .setUri(DiscordEndpoint.GATEWAY.createUri())
                .executeAs(node -> node.get(
                        "url")
                        .asText())
                .exceptionally(throwable -> {
                    logger.exception(throwable);
                    return "wss://gateway.discord.gg"; // default
                    // gateway if gateway couldn't be retrieved
                })
                .thenApply(spec -> {
                    try {
                        return new URL(spec);
                    } catch (MalformedURLException e) {
                        throw new IllegalArgumentException(e);
                    }
                })
                .join();
        Request request = new Request.Builder()
                .url(gatewayUrl)
                .header("User-Agent", "DiscordBot (" + CrystalShard.URL + ", 0.1)")
                .header("Content-Type", "application/json")
                .header("Authorization", discordObject.getPrefixedToken())
                .build();
        this.threadPool = new ThreadPoolImpl(discordObject, 1, "WebSocketClient");
        this.discord = discordObject;
        this.webSocket = CLIENT.newWebSocket(request, new WebSocketListener(discordObject));
        identification();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> webSocket.close(1000, "Shutting down!")));
    }

    private void identification() {
        ObjectNode data = JsonHelper.objectNode("properties",
                JsonHelper.objectNode("$os",
                        JsonHelper.nodeOf(System.getProperty("os.name")),
                        "$browser",
                        JsonHelper.nodeOf(CrystalShard.SHORT_FOOTPRINT),
                        "$device",
                        JsonHelper.nodeOf(CrystalShard.SHORT_FOOTPRINT)));
        sendPayload(Payload.create(OpCode.IDENTIFY, data)).exceptionally(logger::exception);
    }

    /**
     * {@inheritDoc}
     * The future returned by this implementation will never finish.
     */
    public CompletableFuture<Void> sendPayload(Payload payload) {
        assert payload != null : "Payload must not be null!";
        this.threadPool.execute(() -> {
            while (lastPacket.get() > (System.currentTimeMillis() - 600)) {
                try {
                    Thread.sleep(50L);
                } catch (InterruptedException e) {
                    logger.exception(e);
                }
            }
            if (lastPacket.get() <= (System.currentTimeMillis() - 500)) {
                logger.trace("Sending Packet with OpCode " + payload.getCode() + " and body: " + payload.getBody());
                boolean attachedFuture = false;
                for (Payload that : payload.split()) {
                    boolean last = that.isLast();
                    that.addNode("token", JsonHelper.nodeOf(discord.getPrefixedToken()));
                    that.addNode("large_threshold", JsonHelper.nodeOf(250));
                    that.addNode("shard", JsonHelper.arrayNode(discord.getShardId(), discord.getShards()));
                    String nodeAsText = that.getSendableNode().toString();
                    webSocket.send(nodeAsText);
                    if (last) lastPacket.set(System.currentTimeMillis());
                    if (!attachedFuture) {
                        attachedFuture = true;
                    }
                }
            }
        });
        return new CompletableFuture<>();
    }

    public void heartbeat() {
        Payload payload = Payload.create(OpCode.HEARTBEAT, JsonHelper.nodeOf(null));
        sendPayload(payload);
        lastHeartbeat.set(System.currentTimeMillis());
    }

    public boolean respondToHeartbeat() {
        if (lastHeartbeat.get() < System.currentTimeMillis() - 4000) {
            return true;
        }
        return false;
    }
}

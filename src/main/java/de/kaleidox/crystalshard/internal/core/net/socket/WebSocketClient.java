package de.kaleidox.crystalshard.internal.core.net.socket;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.main.CrystalShard;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.logging.Logger;
import de.kaleidox.util.JsonHelper;
import de.kaleidox.crystalshard.internal.core.net.request.Endpoint;
import de.kaleidox.crystalshard.internal.core.net.request.Method;
import de.kaleidox.crystalshard.internal.core.net.request.Payload;
import de.kaleidox.crystalshard.internal.core.net.request.WebRequest;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class WebSocketClient {
    private static final Logger logger = new Logger(WebSocketClient.class);
    private static final HttpClient CLIENT = HttpClient.newHttpClient();
    private final DiscordInternal discord;
    private final WebSocket webSocket;

    public WebSocketClient(Discord discordObject) {
        JsonNode welcomeNode = new WebRequest<String>(
                discordObject,
                Method.GET,
                Endpoint.of(Endpoint.Location.GATEWAY),
                JsonHelper.nodeOf(null))
                .execute()
                .exceptionally(logger::exception)
                .join();
        this.discord = (DiscordInternal) discordObject;
        this.webSocket = CLIENT.newWebSocketBuilder()
                .header("Authorization", discordObject.getPrefixedToken())
                .buildAsync(URI.create(welcomeNode.get("url").asText()), new WebSocketListener((DiscordInternal) discordObject))
                .join();
        identification();
    }

    public CompletableFuture<WebSocket> sendPayload(Payload payload) {
        assert payload != null : "Payload must not be null!";
        CompletableFuture<WebSocket> future = CompletableFuture.failedFuture(new UnknownError("An unknown error" +
                " occurred when trying to send payload: " + payload + "\n" +
                "Please contact the developer."));
        List<Payload> splitLoads = payload.split();
        logger.trace("Sending Packet with OpCode " + payload.getCode() + " and body: " + payload.getBody());
        for (int i = 0; i < splitLoads.size(); i++) {
            Payload that = splitLoads.get(i);
            if (i == splitLoads.size() - 1) {
                CharSequence nodeAsText = that.getSendableNote();
                future = webSocket.sendText(nodeAsText, true);
            } else {
                CharSequence nodeAsText = that.getSendableNote();
                webSocket.sendText(nodeAsText, false);
            }
        }
        return future;
    }

    private void identification() {
        ObjectNode data = JsonHelper.objectNode();
        data.set("token", JsonHelper.nodeOf(discord.getPrefixedToken()));
        ObjectNode properties = (ObjectNode) data.set("properties", JsonHelper.objectNode());
        properties.set("$os", JsonHelper.nodeOf(System.getProperty("os.name")));
        properties.set("$browser", JsonHelper.nodeOf(CrystalShard.SHORT_FOOTPRINT));
        properties.set("$device", JsonHelper.nodeOf(CrystalShard.SHORT_FOOTPRINT));
        data.set("large_threshold", JsonHelper.nodeOf(250));
        data.set("shard", JsonHelper.arrayNode(discord.getShardId(), discord.getShards()));
        sendPayload(Payload.create(OpCode.IDENTIFY, data))
                .exceptionally(logger::exception);
    }

    public void heartbeat() {
        sendPayload(Payload.create(OpCode.HEARTBEAT, JsonHelper.nodeOf(null)))
                .exceptionally(logger::exception);
    }
}

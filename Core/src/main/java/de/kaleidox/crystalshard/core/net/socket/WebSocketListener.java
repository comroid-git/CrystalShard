package de.kaleidox.crystalshard.core.net.socket;

import java.net.http.WebSocket;
import java.nio.ByteBuffer;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.Log;
import de.kaleidox.util.helpers.JsonHelper;

import org.apache.logging.log4j.Logger;

@SuppressWarnings("ConstantConditions")
public class WebSocketListener implements WebSocket.Listener {
    private final static Logger logger = Log.get(WebSocketListener.class);
    private final Discord discord;
    private StringBuilder onTextBuilder = new StringBuilder();
    private CompletableFuture<String> onTextFuture = new CompletableFuture<>();

    public WebSocketListener(Discord discordObject) {
        this.discord = discordObject;
    }

    @Override
    public void onOpen(WebSocket webSocket) {
        webSocket.request(1);
        logger.trace("WebSocket opened: " + webSocket);
    }

    @Override
    public CompletionStage<String> onText(WebSocket webSocket, CharSequence data, boolean last) {
        webSocket.request(1);
        onTextBuilder.append(data);
        if (last) {
            onTextFuture.complete(onTextBuilder.toString());
            onTextBuilder = new StringBuilder();
            CompletableFuture<String> returning = onTextFuture;
            returning.thenAcceptAsync(logger::debug);
            returning.exceptionally(Log.exceptionLogger())
                    .thenApplyAsync(JsonHelper::parse)
                    .thenAcceptAsync(node -> ((WebSocketClientImpl) discord.getWebSocket()).getDispatch().socket(node));
            onTextFuture = new CompletableFuture<>();
            return returning;
        }
        return onTextFuture;
    }

    @Override
    public CompletionStage<?> onBinary(WebSocket webSocket, ByteBuffer data, boolean last) {
        webSocket.request(1);
        logger.error("WebSocket#onBinary is not implemented! Please contact the developer.");
        return null;
    }

    @Override
    public CompletionStage<?> onPing(WebSocket webSocket, ByteBuffer message) {
        webSocket.request(1);
        logger.error("WebSocket#onPing is not implemented! Please contact the developer.");
        return null;
    }

    @Override
    public CompletionStage<?> onPong(WebSocket webSocket, ByteBuffer message) {
        webSocket.request(1);
        logger.error("WebSocket#onPong is not implemented! Please contact the developer.");
        return null;
    }

    @Override
    public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
        webSocket.request(1);
        logger.trace("WebSocket closed with code \'" + statusCode + "\' and reason: " + reason);
        return null;
    }

    @Override
    public void onError(WebSocket webSocket, Throwable error) {
        webSocket.request(1);
        logger.catching(error);
    }
}
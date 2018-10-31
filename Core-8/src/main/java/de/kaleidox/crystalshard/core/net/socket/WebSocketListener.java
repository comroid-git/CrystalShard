package de.kaleidox.crystalshard.core.net.socket;

import de.kaleidox.crystalshard.logging.Logger;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.util.helpers.JsonHelper;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import okhttp3.Response;
import okhttp3.WebSocket;
import okio.ByteString;

public class WebSocketListener extends okhttp3.WebSocketListener {
    private final static Logger logger = new Logger(WebSocketListener.class);
    private final Discord discord;
    private StringBuilder onTextBuilder = new StringBuilder();
    private CompletableFuture<String> onTextFuture = new CompletableFuture<>();

    public WebSocketListener(Discord discordObject) {
        super();
        this.discord = discordObject;
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        super.onOpen(webSocket, response);
        logger.trace("WebSocket opened: " + webSocket);
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        super.onMessage(webSocket, text);
        logger.exception(new WebSocketException(), "WebSocket error");
    }

    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
        super.onMessage(webSocket, bytes);
        DiscordEventDispatch.handle(discord, JsonHelper.parse(bytes.string(Charset.forName("UTF-8"))));
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        super.onClosing(webSocket, code, reason);
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        super.onClosed(webSocket, code, reason);
        logger.trace("WebSocket closed with code \'" + code + "\' and reason: " + reason);
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        super.onFailure(webSocket, t, response);
    }
}

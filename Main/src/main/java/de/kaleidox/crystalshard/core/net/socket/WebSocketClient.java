package de.kaleidox.crystalshard.core.net.socket;

import java.net.http.WebSocket;
import java.util.concurrent.CompletableFuture;

public interface WebSocketClient {
    CompletableFuture<WebSocket> sendPayload(Payload payload);
}

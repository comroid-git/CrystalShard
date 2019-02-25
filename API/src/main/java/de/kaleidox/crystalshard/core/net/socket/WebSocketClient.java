package de.kaleidox.crystalshard.core.net.socket;

import java.util.concurrent.CompletableFuture;

public interface WebSocketClient {
    CompletableFuture<Void> sendPayload(Payload payload);
}

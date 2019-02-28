package de.kaleidox.crystalshard.core.net.socket.handler;

import java.util.concurrent.CompletableFuture;

import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.api.entity.user.Self;
import de.kaleidox.crystalshard.core.net.socket.Dispatch.Handler;
import de.kaleidox.crystalshard.core.net.socket.WebSocketClientImpl;

import com.fasterxml.jackson.databind.JsonNode;

public class READY extends Handler {
    private final CompletableFuture<Self> selfFuture;

    protected READY(Discord discord) {
        super(discord);

        selfFuture = ((WebSocketClientImpl) discord.getWebSocket()).getSelfFuture();
    }

    @Override
    public void submit(JsonNode data) {
    }
}

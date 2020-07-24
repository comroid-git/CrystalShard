package org.comroid.crystalshard.core.gateway;

import org.comroid.crystalshard.DiscordBot;
import org.comroid.crystalshard.core.gateway.event.GatewayEvent;
import org.comroid.crystalshard.core.gateway.event.GatewayPayload;
import org.comroid.listnr.EventManager;
import org.comroid.listnr.impl.ChildEventManager;
import org.comroid.mutatio.ref.FutureReference;
import org.comroid.restless.socket.WebSocket;
import org.comroid.restless.socket.event.WebSocketPayload;

import java.util.concurrent.Executor;

public final class Gateway extends ChildEventManager<DiscordBot, WebSocketPayload.Data, GatewayEvent<? extends GatewayPayload>, GatewayPayload> {
    private final WebSocket webSocket;
    private final FutureReference<DiscordBot> botRef;

    @Override
    public DiscordBot getDependent() {
        return botRef.requireNonNull("Dependency Object");
    }

    public WebSocket getWebSocket() {
        return webSocket;
    }

    public Gateway(FutureReference<DiscordBot> botRef, Executor executor, WebSocket webSocket) {
        //noinspection unchecked
        super(
                executor,
                new EventManager[]{webSocket},
                GatewayEvent.cache.values().toArray(new GatewayEvent[0])
        );

        this.botRef = botRef;
        this.webSocket = webSocket;
    }
}

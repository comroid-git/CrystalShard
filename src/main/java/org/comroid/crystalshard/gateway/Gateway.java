package org.comroid.crystalshard.gateway;

import lombok.Getter;
import lombok.SneakyThrows;
import org.comroid.api.Initializable;
import org.comroid.crystalshard.DiscordBotShard;
import org.comroid.crystalshard.model.gateway.GatewayBotResponse;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.web.socket.*;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.messaging.SubProtocolWebSocketHandler;

import java.io.Closeable;
import java.io.IOException;

public class Gateway implements Closeable, Initializable, WebSocketHandler {
    private static final Logger logger = LoggerFactory.getLogger(Gateway.class);
    @Getter
    private final DiscordBotShard shard;
    private final StandardWebSocketClient socket;
    private final SubProtocolWebSocketHandler handler;

    @SneakyThrows
    public Gateway(DiscordBotShard shard, GatewayBotResponse gbr) {

        this.shard = shard;
        this.socket = new StandardWebSocketClient();
        this.handler = new SubProtocolWebSocketHandler(EventChannel.Socket, EventChannel.Gateway);
        handler.addProtocolHandler(new GatewayProtocolHandler(this));
        socket.execute(handler, createHeaders(), gbr.getUrl().toURI());
    }

    private WebSocketHttpHeaders createHeaders() {
        return new WebSocketHttpHeaders();
    }

    @Override
    public void close() throws IOException {

    }

    @Override
    public void initialize() throws Throwable {

    }

    @Override
    public void afterConnectionEstablished(@NotNull WebSocketSession session) throws Exception {
        handler.afterConnectionEstablished(session);
    }

    @Override
    public void handleMessage(@NotNull WebSocketSession session, @NotNull WebSocketMessage<?> message) throws Exception {
        handler.handleMessage(session, message);
    }

    @Override
    public void handleTransportError(@NotNull WebSocketSession session, @NotNull Throwable exception) throws Exception {
        handler.handleTransportError(session, exception);
    }

    @Override
    public void afterConnectionClosed(@NotNull WebSocketSession session, @NotNull CloseStatus closeStatus) throws Exception {
        handler.afterConnectionClosed(session, closeStatus);
    }

    @Override
    public boolean supportsPartialMessages() {
        return handler.supportsPartialMessages();
    }

    public boolean isReady() {
    }
}

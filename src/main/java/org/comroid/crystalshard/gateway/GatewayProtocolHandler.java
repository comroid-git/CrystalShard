package org.comroid.crystalshard.gateway;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.messaging.SubProtocolHandler;

import java.util.List;

public class GatewayProtocolHandler implements SubProtocolHandler {
    private final Gateway gateway;
    private final ObjectMapper objectMapper;

    @Override
    public List<String> getSupportedProtocols() {
        return List.of("Discord Gateway");
    }

    public GatewayProtocolHandler(Gateway gateway) {
        this.gateway = gateway;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void handleMessageFromClient(@NotNull WebSocketSession session, @NotNull WebSocketMessage<?> message, @NotNull MessageChannel outputChannel) throws Exception {
    }

    @Override
    public void handleMessageToClient(@NotNull WebSocketSession session, @NotNull Message<?> message) throws Exception {
    }

    @Override
    public String resolveSessionId(@NotNull Message<?> message) {
        return null;
    }

    @Override
    public void afterSessionStarted(@NotNull WebSocketSession session, @NotNull MessageChannel outputChannel) throws Exception {

    }

    @Override
    public void afterSessionEnded(@NotNull WebSocketSession session, @NotNull CloseStatus closeStatus, @NotNull MessageChannel outputChannel) throws Exception {

    }
}

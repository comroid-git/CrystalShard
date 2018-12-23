package de.kaleidox.crystalshard.core.net.socket;

import com.fasterxml.jackson.databind.JsonNode;

import de.kaleidox.crystalshard.core.concurrent.ThreadPoolImpl;
import de.kaleidox.crystalshard.internal.InternalInjector;
import de.kaleidox.crystalshard.logging.Logger;
import de.kaleidox.crystalshard.main.Discord;

public class DiscordEventDispatch {
    private final static Logger logger = new Logger(DiscordEventDispatch.class);

    public static void handle(Discord discord, JsonNode data) {
        WebSocketClientImpl webSocket = (WebSocketClientImpl) discord.getWebSocket();
        OpCode.getByCode(data.get("op")
                .asInt())
                .ifPresent(opCode -> {
                    logger.trace("Recieved Packet with OpCode " + opCode + " and body: " + data.toString());
                    switch (opCode) {
                        case HELLO:
                            long heartbeat_interval = (data.get("d")
                                    .get("heartbeat_interval")
                                    .asLong());
                            ((ThreadPoolImpl) discord.getThreadPool()).startHeartbeat(heartbeat_interval);
                            break;
                        case DISPATCH:
                            InternalInjector.tryHandle(discord, data);
                            break;
                        case HEARTBEAT:
                            webSocket.heartbeat();
                            break;
                        case HEARTBEAT_ACK:
                            if (webSocket.respondToHeartbeat()) {
                                webSocket.heartbeat();
                            }
                            break;
                        default:
                            logger.warn("Unexpected OpCode recieved: " + opCode + " with body: " + data + "\n" + "Please inform the developer!");
                            break;
                    }
                });
    }
}

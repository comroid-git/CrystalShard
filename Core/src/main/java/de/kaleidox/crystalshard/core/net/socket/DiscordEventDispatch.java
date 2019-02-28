package de.kaleidox.crystalshard.core.net.socket;

import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.api.util.Log;
import de.kaleidox.crystalshard.core.concurrent.ThreadPoolImpl;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.logging.log4j.Logger;

public class DiscordEventDispatch {
    private final static Logger logger = Log.get(DiscordEventDispatch.class);

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
                            //InternalInjector.tryHandle(discord, data); // todo
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
                            logger.warn("Unexpected OpCode recieved: " + opCode +
                                    " with body: " + data + "\n" + "Please inform the developer!");
                            break;
                    }
                });
    }
}

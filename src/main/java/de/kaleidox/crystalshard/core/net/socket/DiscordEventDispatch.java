package de.kaleidox.crystalshard.core.net.socket;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.handling.handlers.HandlerBase;
import de.kaleidox.logging.Logger;

public class DiscordEventDispatch {
    private final static Logger logger = new Logger(DiscordEventDispatch.class);
    
// Static members
    // Static membe
    public static void handle(DiscordInternal discord, JsonNode data) {
        WebSocketClient webSocket = discord.getWebSocket();
        OpCode.getByCode(data.get("op").asInt()).ifPresent(opCode -> {
            logger.trace("Recieved Packet with OpCode " + opCode + " and body: " + data.toString());
            switch (opCode) {
                case HELLO:
                    long heartbeat_interval = (data.get("d").get("heartbeat_interval").asLong());
                    discord.getThreadPool().startHeartbeat(heartbeat_interval);
                    break;
                case DISPATCH:
                    discord.getThreadPool().execute(() -> HandlerBase.tryHandle(discord, data));
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
                    logger.warn("Unexpected OpCode recieved: " + opCode + " with body: " + data + "\n" +
                                "Please inform the developer!");
                    break;
            }
        });
    }
}

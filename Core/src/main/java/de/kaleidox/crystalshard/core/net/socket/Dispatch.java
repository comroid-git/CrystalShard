package de.kaleidox.crystalshard.core.net.socket;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import de.kaleidox.crystalshard.Log;
import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.api.entity.user.Self;
import de.kaleidox.crystalshard.core.concurrent.ThreadPoolImpl;
import de.kaleidox.crystalshard.core.net.socket.handler.READY;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.logging.log4j.Logger;
import org.intellij.lang.annotations.MagicConstant;

public class Dispatch {
    public static final Logger logger = Log.get(Dispatch.class);

    private final Discord discord;
    private final WebSocketClientImpl webSocketClient;
    private final ConcurrentHashMap<String, Handler> handlers;

    final CompletableFuture<Self> selfFuture;

    public Dispatch(Discord discord, WebSocketClientImpl webSocketClient) {
        this.discord = discord;
        this.webSocketClient = webSocketClient;

        handlers = new ConcurrentHashMap<>();
        selfFuture = new CompletableFuture<>();
    }

    public void socket(JsonNode data) {
        WebSocketClientImpl webSocket = (WebSocketClientImpl) discord.getWebSocket();
        @MagicConstant(valuesFromClass = OpCode.class) int op = data.get("op").asInt();
        logger.trace("Recieved Packet with OpCode " + op + " and body: " + data.toString());
        switch (op) {
            case OpCode.DISPATCH:
                event(data);
                break;
            case OpCode.HEARTBEAT:
                webSocket.heartbeat();
                break;
            case OpCode.HELLO:
                long heartbeat_interval = (data.get("d").get("heartbeat_interval").asLong());
                ((ThreadPoolImpl) discord.getThreadPool()).startHeartbeat(heartbeat_interval);
                break;
            case OpCode.HEARTBEAT_ACK:
                if (webSocket.respondToHeartbeat()) {
                    webSocket.heartbeat();
                }
                break;
            default:
                logger.warn("Unexpected OpCode recieved: " + op
                        + " with body: " + data + "\n" + "Please inform the developer!");
                break;
        }
    }

    public void event(JsonNode data) {
        final JsonNode d = data.get("d");
        final String t = data.get("t").asText();

        if (handlers.containsKey(t)) {
            Handler handler = handlers.get(t);
            handler.submit(d);
        } else {
            try {
                Class<?> klasse = Class.forName(READY.class.getPackage().getName() + "." + t);
                Handler h = (Handler) klasse.getConstructor(Discord.class).newInstance(discord);
                handlers.put(t, h);
            } catch (ClassNotFoundException | NoSuchMethodException e) {
                logger.fatal(new Exception("Could not find handler class! Contact the developer.", e));
                System.exit(1);
            } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                logger.fatal(new Exception("Could not create new handler instance!", e));
            }
        }
    }

    public static abstract class Handler {
        private final Discord discord;

        protected Handler(Discord discord) {
            this.discord = discord;
        }

        public abstract void submit(JsonNode data);
    }
}

package de.kaleidox.crystalshard.internal.core.net;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.core.handlers.HandlerBase;
import de.kaleidox.crystalshard.internal.core.net.socket.OpCode;
import de.kaleidox.logging.Logger;
import de.kaleidox.util.helpers.JsonHelper;

import java.lang.reflect.InvocationTargetException;

public class DiscordEventDispatch {
    private final static Logger logger = new Logger(DiscordEventDispatch.class);

    public static void handle(DiscordInternal discord, JsonNode data) {
        OpCode.getByCode(data.get("op").asInt())
                .ifPresent(opCode -> {
                    JsonNode node = data.get("d");
                    switch (opCode) {
                        case HELLO:
                            long heartbeat_interval = (node.get("heartbeat_interval").asLong());
                            discord.getThreadPool().startHeartbeat(heartbeat_interval);
                            break;
                        case DISPATCH:
                            discord.getThreadPool().execute(() -> dispatch(discord, data));
                            break;
                        case HEARTBEAT:
                            discord.getWebSocket().heartbeat();
                            break;
                        default:
                            logger.warn("Unexpected OpCode recieved: " + opCode + " with body: " + data + "\n" +
                                    "Please inform the developer!");
                            break;
                    }
                });
    }

    private static <T extends HandlerBase> void dispatch(DiscordInternal discord, JsonNode data) {
        try {
            logger.trace("Dispatching event '"+data.get("t").asText()+"' with body: "+data.get("d").toString());
            Package handlerPackage = HandlerBase.class.getPackage();
            String t1 = handlerPackage.getName() + "." + data.get("t").asText();
            Class<T> tClass = (Class<T>) Class.forName(t1);
            HandlerBase t = tClass.getConstructor().newInstance();
            discord.getThreadPool().execute(() -> t.handle(data.get("d")));
        } catch (ClassNotFoundException e) {
            logger.exception(e, "Failed to dispatch unknown type: " + data.get("t"));
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            logger.exception(e, "Failed to create instance of handler: "+data.get("t"));
        }
    }
}

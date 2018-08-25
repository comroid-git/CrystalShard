package de.kaleidox.crystalshard.internal.core.net;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.logging.Logger;
import de.kaleidox.util.JsonHelper;
import de.kaleidox.websocket.OpCode;

import java.util.concurrent.CompletableFuture;

public class DiscordEventDispatch {
    private final static Logger logger = new Logger(DiscordEventDispatch.class);

    public static void handle(DiscordInternal discord, CompletableFuture<String> returning) {
        returning.thenAcceptAsync(string -> {
            JsonNode data = JsonHelper.parse(string);

            OpCode.getByCode(data.get("op").asInt())
                    .ifPresent(opCode -> {
                        JsonNode node = data.get("d");
                        switch (opCode) {
                            case HELLO:
                                long heartbeat_interval = (node.get("heartbeat_interval").asLong());
                                discord.getThreadPool().initScheduler(heartbeat_interval);
                                break;
                            case DISPATCH:
                                discord.getThreadPool().execute(() -> dispatch(discord, data));
                                break;
                            default:
                                logger.warn("Unexpected OpCode recieved: " + opCode + " with body: " + data + "\n" +
                                        "Please inform the developer!");
                                break;
                        }
                    });
        }).exceptionally(logger::exception);
    }

    private static void dispatch(DiscordInternal discord, JsonNode data) {
        switch (data.get("t").asText(null)) {
            case "GUILD_CREATE":
                discord.craftServer(data.get("d"));
                break;
            default:
                logger.warn("Tried to dispatch unknown listener type: " + data.get("t"));
                break;
        }
    }
}

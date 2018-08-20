package de.kaleidox.crystalshard.internal.core;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.internal.items.DiscordInternal;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.logging.Logger;
import de.kaleidox.util.JsonHelper;
import de.kaleidox.websocket.OpCode;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class DiscordEventDispatch {
    private final static Logger logger = new Logger(DiscordEventDispatch.class);

    public static void dispatch(DiscordInternal discord, CompletableFuture<String> returning) {
        returning.thenAcceptAsync(string -> {
            JsonNode data = JsonHelper.parse(string);

            OpCode.getByCode(Objects.requireNonNull(data).get("op").asInt())
                    .ifPresent(opCode -> {
                        JsonNode node = data.get("d");
                        switch (opCode) {
                            case HELLO:
                                long heartbeat_interval = (node.get("heartbeat_interval").asLong()/10);
                                discord.getThreadPool().initScheduler(heartbeat_interval);
                                break;
                            default:
                                logger.warn("Unexpected OpCode recieved: "+opCode+" with body: "+data+"\n" +
                                        "Please inform the developer!");
                                break;
                        }
                    });
        }).exceptionally(logger::exception);
    }
}

package de.kaleidox.crystalshard.internal.core;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.internal.exception.DiscordResponseException;
import de.kaleidox.logging.Logger;

import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class ResponseDispatch {
    private final static Logger logger = new Logger(ResponseDispatch.class);

    public static boolean dispatch(HttpResponse<String> response,
                                   CompletableFuture<JsonNode> future) {
        logger.trace("Dispatching HttpResponse with status code "+response.statusCode()+
                " and body: " +response.body());
        switch (response.statusCode()) {
            case 200:
                return true;
            default:
                future.completeExceptionally(
                        new DiscordResponseException("Discord Responded with unknown status code " +
                        response.statusCode()+" and message: " + response.body()));
                break;
        }
        return false;
    }
}

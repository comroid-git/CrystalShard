package de.kaleidox.crystalshard.internal.core.net;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.core.net.request.Ratelimiting;
import de.kaleidox.crystalshard.main.exception.DiscordResponseException;
import de.kaleidox.logging.Logger;
import de.kaleidox.util.JsonHelper;

import java.net.http.HttpHeaders;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class ResponseDispatch {
    private final static Logger logger = new Logger(ResponseDispatch.class);

    public static <T> boolean dispatch(DiscordInternal discord,
                                       HttpResponse<String> response,
                                       CompletableFuture<T> future) {
        String body = response.body();
        JsonNode data = JsonHelper.parse(body);
        Ratelimiting ratelimiter = discord.getRatelimiter();
        HttpHeaders headers = response.headers();
        logger.trace("Dispatching HttpResponse with status code " + response.statusCode() + " and body: " + body);
        try {
            headers.firstValue("Retry-After").map(Long::parseLong).ifPresent(ratelimiter::retryAfter);
            headers.firstValue("X-RateLimit-Limit").map(Long::parseLong).ifPresent(ratelimiter::setLimit);
            headers.firstValue("X-RateLimit-Remaining").map(Long::parseLong).ifPresent(ratelimiter::retryAfter);
            headers.firstValue("X-RateLimit-Globl").map(Boolean::valueOf).ifPresent(ratelimiter::setGlobal);
        } catch (NullPointerException ignored) {
        }
        switch (response.statusCode()) {
            case 200:
                return true;
            case 429:
                logger.warn("Warning: " + data.get("message"));
                ratelimiter.retryAfter(data.get("retry_after").asLong());
                ratelimiter.setGlobal(data.get("global").asBoolean());
                return false;
            default:
                future.completeExceptionally(
                        new DiscordResponseException("Discord Responded with unknown status code " +
                                response.statusCode() + " and message: " + body));
                break;
        }
        return false;
    }
}

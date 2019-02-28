package de.kaleidox.crystalshard.core.net.request;

import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.api.exception.DiscordResponseException;
import de.kaleidox.crystalshard.core.DiscordImpl;
import de.kaleidox.crystalshard.core.net.request.endpoint.DiscordRequestURI;
import de.kaleidox.crystalshard.core.net.request.endpoint.RequestURI;
import de.kaleidox.crystalshard.core.net.request.ratelimit.RatelimiterImpl;
import de.kaleidox.util.CompletableFutureExtended;
import de.kaleidox.util.helpers.JsonHelper;

import com.fasterxml.jackson.databind.JsonNode;

public class DiscordRequestImpl<T> extends WebRequestImpl<T> {
    private final Discord discord;

    public DiscordRequestImpl(Discord discord) {
        super();
        this.discord = discord;
    }

    @Override
    public CompletableFuture<String> execute() throws RuntimeException {
        Objects.requireNonNull(uri, "URI is not set!");
        Objects.requireNonNull(method, "Method is not set!");

        CompletableFuture<String> future = new CompletableFutureExtended<>(discord.getExecutor());
        CompletableFuture<HttpHeaders> headersFuture = new CompletableFutureExtended<>(discord.getExecutor());
        RatelimiterImpl ratelimiter = (RatelimiterImpl) discord.getRatelimiter();
        ratelimiter.schedule(this, headersFuture, () -> {
            try {
                String body = (method == HttpMethod.GET ? "" : (node == null ? "" : node.toString()));
                logger.trace("Creating request: " + toString() + " with request body: " + body);
                HttpRequest request = requestBuilder.uri(uri.getURI())
                        .header("User-Agent", "DiscordBot (http://kaleidox.de, 0.1)")
                        .header("Content-Type", "application/json")
                        .header("Authorization", discord)
                        .method(method.getDescriptor(), HttpRequest.BodyPublishers.ofString(body))
                        .build();
                HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
                String responseBody = response.body();
                int statusCode = response.statusCode();
                headersFuture.complete(response.headers());
                boolean unknown = true;
                switch (statusCode) {
                    case 200: // 200 OK
                    case 204: // 204 OK is a successful DELETE method
                        future.complete(responseBody);
                        break;
                    case 400: // 400 Bad Request
                        logger.error(
                                "{400} Bad Request issued: " + method.getDescriptor() + " " + uri.getURI() + " with response responseBody: " + responseBody +
                                        " and request responseBody: " + body);
                        break;
                    case 429: // 429 Ratelimited
                        logger.warn("{429} Warning: Ratelimit was hit with request: " + toString() + ". Response was:" + responseBody +
                                "\n\t\tPlease contact the developer!");
                        // wait for ratelimiter retry
                        return;
                    // Non-Unknown codes, yet dont need special treatment:
                    case 404: // Not Found
                    case 403: // Missing Access
                        unknown = false;
                    default: // Anything else
                        JsonNode responseNode = JsonHelper.parse(responseBody);
                        logger.error(
                                "{" + statusCode + ":" + responseNode.get("code")
                                        .asText() + ":\"" + responseNode.get("message")
                                        .asText() + "\"} " +
                                        (unknown ? "Recieved unknown status code from Discord" + " " + "with responseBody: " + responseBody :
                                                "Untreated code recieved with body: " + responseBody));
                        future.completeExceptionally(new DiscordResponseException(
                                "Discord Responded with unknown status code " + statusCode + " and message: " + responseBody));
                        break;
                }
            } catch (Throwable e) {
                logger.error("Error in WebRequest " + toString() + ": " + e.getMessage());
            }
        });

        return future;
    }

    @Override
    public String toString() {
        return "DiscordRequest -> " + uri.toString();
    }
}

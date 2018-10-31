package de.kaleidox.crystalshard.core.net.request;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.core.net.request.ratelimit.RatelimiterImpl;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.exception.DiscordResponseException;
import de.kaleidox.crystalshard.util.helpers.JsonHelper;
import de.kaleidox.crystalshard.util.objects.CompletableFutureExtended;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DiscordRequestImpl<T> extends WebRequestImpl<T> {
    private final Discord discord;

    public DiscordRequestImpl(Discord discord) {
        super();
        this.discord = discord;
    }

    public Request getRequest() {
        String body = (method == HttpMethod.GET ? "" : node.toString());
        return requestBuilder.url(uri.getURI().toString())
                .header("User-Agent", "DiscordBot (http://kaleidox.de, 0.1)")
                .header("Content-Type", "application/json")
                .header("Authorization", discord.getPrefixedToken())
                .method(method.getDescriptor(), RequestBody.create(
                        MediaType.parse("application/json; charset=utf-8"), body))
                .build();
    }

    @Override
    public String toString() {
        return "DiscordRequest -> " + uri.toString();
    }

    @Override
    public CompletableFuture<String> execute() throws RuntimeException {
        Objects.requireNonNull(uri, "URI is not set!");
        Objects.requireNonNull(method, "Method is not set!");
        Objects.requireNonNull(node, "Node is not set!");

        CompletableFuture<String> future = new CompletableFutureExtended<>(discord.getExecutor());
        CompletableFuture<Headers> headersFuture = new CompletableFutureExtended<>(discord.getExecutor());
        RatelimiterImpl ratelimiter = (RatelimiterImpl) discord.getRatelimiter();
        ratelimiter.schedule(this, headersFuture, () -> {
            try {
                String body = (method == HttpMethod.GET ? "" : node.toString());
                logger.trace("Creating request: " + toString() + " with request body: " + body);
                Request request = requestBuilder.url(uri.getURI().toString())
                        .header("User-Agent", "DiscordBot (http://kaleidox.de, 0.1)")
                        .header("Content-Type", "application/json")
                        .header("Authorization", discord.getPrefixedToken())
                        .method(method.getDescriptor(), RequestBody.create(
                                MediaType.parse("application/json; charset=utf-8"), body))
                        .build();
                Response response = CLIENT.newCall(request).execute();
                assert response.body() != null : "Unexcpected NullPointer in DiscordRequestImpl";
                String responseBody = response.body().string();
                int statusCode = response.code();
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
                        logger.traceElseInfo(
                                "{" + statusCode + ":" + responseNode.get("code")
                                        .asText() + ":\"" + responseNode.get("message")
                                        .asText() + "\"} " +
                                        (unknown ? "Recieved unknown status code from Discord" + " " + "with responseBody: " + responseBody :
                                                "Untreated code recieved with body: " + responseBody), "Recieved unknown status code: " + statusCode);
                        future.completeExceptionally(new DiscordResponseException(
                                "Discord Responded with unknown status code " + statusCode + " and message: " + responseBody));
                        break;
                }
            } catch (Throwable e) {
                logger.exception(e, "Error in WebRequest " + toString());
            }
        });

        return future;
    }
}

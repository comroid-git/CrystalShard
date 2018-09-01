package de.kaleidox.crystalshard.internal.core.net.request;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.core.net.ResponseDispatch;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.logging.Logger;
import de.kaleidox.util.CompletableFutureExtended;
import de.kaleidox.util.helpers.JsonHelper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * This class is used for creating requests to discord.
 *
 * @param <T> The output item. The output type comes from JSON and is mapped using {@link #execute(Function)}.
 */
public class WebRequest<T> {
    private static final Logger logger = new Logger(WebRequest.class);
    private static final String BASE_URL = "https://discordapp.com/api";
    private static final HttpClient CLIENT = HttpClient.newHttpClient();
    private final CompletableFutureExtended<T> future;
    private DiscordInternal discord;
    private JsonNode node;
    private Endpoint endpoint;
    private Method method;

    public WebRequest(Discord discord) {
        this(null, (DiscordInternal) discord, null, null, null);
    }

    public WebRequest(Discord discord,
                      Method method,
                      Endpoint endpoint,
                      JsonNode node) {
        this(null, (DiscordInternal) discord, method, endpoint, node);
    }


    public WebRequest(CompletableFutureExtended<T> future,
                      DiscordInternal discord,
                      Method method,
                      Endpoint endpoint,
                      JsonNode node) {
        if (Objects.nonNull(future)) {
            this.future = future;
        } else {
            this.future = new CompletableFutureExtended<>(discord.getThreadPool());
        }
        this.discord = discord;
        this.method = method;
        this.endpoint = endpoint;
        this.node = node;
    }

    public WebRequest<T> discord(Discord discord) {
        this.discord = (DiscordInternal) discord;
        return this;
    }

    public WebRequest<T> endpoint(Endpoint endpoint) {
        this.endpoint = endpoint;
        return this;
    }

    public WebRequest<T> node(JsonNode node) {
        this.node = node;
        return this;
    }

    public WebRequest<T> method(Method method) {
        this.method = method;
        return this;
    }

    public CompletableFutureExtended<T> getFuture() {
        return future;
    }

    public CompletableFuture<T> execute(Function<JsonNode, T> requestBodyMapper) {
        return execute().thenApply(requestBodyMapper);
    }

    public CompletableFutureExtended<JsonNode> execute() {
        return request(method, endpoint, node);
    }

    @SuppressWarnings({"SameParameterValue", "unchecked"})
    private CompletableFutureExtended<JsonNode> request(
            Method method,
            Endpoint endpoint,
            JsonNode data) {
        Objects.requireNonNull(method, "Method must not be null.");
        Objects.requireNonNull(endpoint, "Endpoint must not be null.");
        if (data == null) data = JsonHelper.objectNode();
        if (data.isNull()) data = JsonHelper.objectNode();
        CompletableFutureExtended<JsonNode> future = new CompletableFutureExtended<>(discord.getThreadPool());
        Ratelimiting ratelimiter = discord.getRatelimiter();
        JsonNode finalData = data;
        ratelimiter.schedule(() -> {
            try {
                String urlExternal = endpoint.getUrl().toExternalForm();
                String dataAsString = finalData.toString();
                logger.trace("Creating request: " + method.getDescriptor() + " " +
                        urlExternal + " with body: " + dataAsString);
                HttpRequest request = HttpRequest
                        .newBuilder()
                        .uri(URI.create(urlExternal))
                        .headers("User-Agent", "DiscordBot (http://kaleidox.de, 0.1)",
                                "Content-Type", "application/json",
                                "Authorization", discord.getPrefixedToken())
                        .method(method.getDescriptor(),
                                HttpRequest.BodyPublishers.ofString(dataAsString))
                        .build();
                HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
                switch (response.statusCode()) {
                    case 429:
                        JsonNode responseNode = JsonHelper.parse(response.body());
                        Ratelimiting.RatelimitBlock block = new Ratelimiting.RatelimitBlock();
                        logger.warn("Warning: " + responseNode.get("message"));
                        block.setRetryAfter(responseNode.get("retry_after").asLong());
                        block.setGlobal(responseNode.get("global").asBoolean());
                        try {
                            HttpHeaders headers = response.headers();
                            headers.firstValue("Retry-After").map(Long::parseLong)
                                    .ifPresent(block::setRetryAfter);
                            headers.firstValue("X-RateLimit-Limit").map(Long::parseLong)
                                    .ifPresent(block::setLimit);
                            headers.firstValue("X-RateLimit-Remaining").map(Long::parseLong)
                                    .ifPresent(block::setRemaining);
                            headers.firstValue("X-RateLimit-Global").map(Boolean::valueOf)
                                    .ifPresent(block::setGlobal);
                        } catch (NullPointerException e) {
                            logger.deeptrace("NPE on Ratelimit Header checking. Message: " + e.getMessage());
                        } finally {
                            future.completeExceptionally(block);
                        }
                        break;
                    case 400:
                        logger.error("{400} Bad Request issued: " + method.getDescriptor() + " " +
                                urlExternal + " with body: " + dataAsString);
                        break;
                    default:
                        logger.trace("Recieved status code " + response.statusCode() +
                                " from Discord with body: " + response.body());
                        boolean dispatch = ResponseDispatch.dispatch(discord, response, future);
                        if (!dispatch & (future.isCancelled() || future.isCompletedExceptionally())) {
                            logger.error("Something went horribly wrong in WebRequest.java, " +
                                    "please contact the developer.");
                        } else {
                            if (dispatch) {
                                future.complete(JsonHelper.parse(response.body()));
                            }
                        }
                        UnknownError unknownError = new UnknownError("An unknown error ocurred.");
                        future.completeExceptionally(unknownError);
                        break;
                }
            } catch (IOException | InterruptedException e) {
                future.completeExceptionally(e);
            }
        });

        return future;
    }

    public Optional<String> getFirstArgument() {
        return null;
    }
}

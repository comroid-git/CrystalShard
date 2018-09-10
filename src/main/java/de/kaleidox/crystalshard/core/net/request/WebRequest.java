package de.kaleidox.crystalshard.core.net.request;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.core.net.ResponseDispatch;
import de.kaleidox.crystalshard.core.net.request.ratelimiting.Ratelimiting;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.logging.Logger;
import de.kaleidox.util.CompletableFutureExtended;
import de.kaleidox.util.helpers.JsonHelper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;
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

    public Endpoint getEndpoint() {
        return endpoint;
    }

    public Method getMethod() {
        return method;
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
        CompletableFutureExtended<HttpHeaders> headersFuture = new CompletableFutureExtended<>(discord.getThreadPool());
        Ratelimiting ratelimiter = discord.getRatelimiter();
        final JsonNode finalData = data;
        ratelimiter.schedule(this, headersFuture, () -> {
            try {
                String urlExternal = endpoint.getUrl().toExternalForm();
                String requestBody = (method == Method.GET ? "" : finalData.toString());
                logger.trace("Creating request: " + method.getDescriptor() + " " +
                        urlExternal + " with body: " + requestBody);
                HttpRequest request = HttpRequest
                        .newBuilder()
                        .uri(URI.create(urlExternal))
                        .headers("User-Agent", "DiscordBot (http://kaleidox.de, 0.1)",
                                "Content-Type", "application/json",
                                "Authorization", discord.getPrefixedToken())
                        .method(method.getDescriptor(),
                                HttpRequest.BodyPublishers.ofString(requestBody))
                        .build();
                HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
                headersFuture.complete(response.headers());
                switch (response.statusCode()) {
                    case 429:
                        JsonNode responseNode = JsonHelper.parse(response.body());
                        logger.warn("Warning: " + responseNode.get("message"));
                        // wait for ratelimiter retry
                        break;
                    case 400:
                        logger.error("{400} Bad Request issued: " + method.getDescriptor() + " " +
                                urlExternal + " with response body: " + response.body() +
                                " and request body: " + requestBody);
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
            } catch (Throwable e) {
                logger.exception(e, "Error in WebRequest @ " + endpoint);
            }
        });

        return future;
    }
}

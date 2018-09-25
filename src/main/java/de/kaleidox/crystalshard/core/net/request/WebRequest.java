package de.kaleidox.crystalshard.core.net.request;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.core.net.request.ratelimiting.Ratelimiting;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.exception.DiscordResponseException;
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
    private static final Logger                       logger   = new Logger(WebRequest.class);
    private static final String                       BASE_URL = "https://discordapp.com/api";
    private static final HttpClient                   CLIENT   = HttpClient.newHttpClient();
    private final        CompletableFutureExtended<T> future;
    private              DiscordInternal              discord;
    private              JsonNode                     node;
    private              Endpoint                     endpoint;
    private              Method                       method;
    
    public WebRequest(Discord discord) {
        this(null, (DiscordInternal) discord, null, null, null);
    }
    
    public WebRequest(Discord discord, Method method, Endpoint endpoint, JsonNode node) {
        this(null, (DiscordInternal) discord, method, endpoint, node);
    }
    
    public WebRequest(CompletableFutureExtended<T> future, DiscordInternal discord, Method method, Endpoint endpoint,
                      JsonNode node) {
        if (Objects.nonNull(future)) {
            this.future = future;
        } else {
            this.future = new CompletableFutureExtended<>(discord.getExecutor());
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
    
    public CompletableFuture<T> executeNull() {
        return execute(data -> null);
    }
    
    public CompletableFuture<T> execute(Function<JsonNode, T> requestBodyMapper) {
        return execute().thenApply(requestBodyMapper);
    }
    
    public CompletableFutureExtended<JsonNode> execute() {
        return request(method, endpoint, node);
    }
    
    private CompletableFutureExtended<JsonNode> request(Method method, Endpoint endpoint, JsonNode data) {
        Objects.requireNonNull(method, "Method must not be null.");
        Objects.requireNonNull(endpoint, "Endpoint must not be null.");
        if (data == null) data = JsonHelper.objectNode();
        if (data.isNull()) data = JsonHelper.objectNode();
        CompletableFutureExtended<JsonNode> future = new CompletableFutureExtended<>(discord.getExecutor());
        CompletableFutureExtended<HttpHeaders> headersFuture = new CompletableFutureExtended<>(discord.getExecutor());
        Ratelimiting ratelimiter = discord.getRatelimiter();
        final JsonNode finalData = data;
        ratelimiter.schedule(this, headersFuture, () -> {
            try {
                String urlExternal = endpoint.getUrl()
                        .toExternalForm();
                String requestBody = (method == Method.GET ? "" : finalData.toString());
                logger.trace(
                        "Creating request: " + method.getDescriptor() + " " + urlExternal + " with responseBody: " +
                        requestBody);
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(urlExternal))
                        .headers("User-Agent",
                                 "DiscordBot (http://kaleidox.de, 0.1)",
                                 "Content-Type",
                                 "application/json",
                                 "Authorization",
                                 discord.getPrefixedToken())
                        .method(method.getDescriptor(), HttpRequest.BodyPublishers.ofString(requestBody))
                        .build();
                HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
                String responseBody = response.body();
                headersFuture.complete(response.headers());
                switch (response.statusCode()) {
                    case 200: // 200 OK
                    case 204: // 204 OK is a successful DELETE method
                        future.complete(JsonHelper.parse(responseBody));
                    case 429: // 429 Ratelimited
                        JsonNode responseNode = JsonHelper.parse(responseBody);
                        logger.warn("Warning: " + responseNode.get("message"));
                        // wait for ratelimiter retry
                        break;
                    case 400: // 400 Bad Request
                        logger.error("{400} Bad Request issued: " + method.getDescriptor() + " " + urlExternal +
                                     " with response responseBody: " + responseBody + " and request responseBody: " +
                                     requestBody);
                        break;
                    default: // Anything else
                        logger.traceElseInfo("Recieved unknown status code " + response.statusCode() +
                                             " from Discord with responseBody: " + responseBody,
                                             "Recieved unknown status code: " + response.statusCode());
                        future.completeExceptionally(new DiscordResponseException(
                                "Discord Responded with unknown status code " + response.statusCode() +
                                " and message: " + responseBody));
                        break;
                }
            } catch (Throwable e) {
                logger.exception(e, "Error in WebRequest @ " + endpoint);
            }
        });
        
        return future;
    }
}

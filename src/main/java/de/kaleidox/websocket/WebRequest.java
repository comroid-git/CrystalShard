package de.kaleidox.websocket;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.internal.core.ResponseDispatch;
import de.kaleidox.crystalshard.internal.items.DiscordInternal;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.logging.Logger;
import de.kaleidox.util.JsonHelper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class WebRequest<T> {
    private static final Logger logger = new Logger(WebRequest.class);
    private static final String BASE_URL = "https://discordapp.com/api";
    private static final HttpClient CLIENT = HttpClient.newHttpClient();
    private DiscordInternal discord;
    private JsonNode node;
    private String endpoint;
    private Method method;

    public WebRequest(Discord discord) {
        this.discord = (DiscordInternal) discord;
    }

    public WebRequest(Discord discord,
                      Method method,
                      String endpoint,
                      JsonNode node) {
        this.discord = (DiscordInternal) discord;
        this.method = method;
        this.endpoint = endpoint;
        this.node = node;
    }

    public WebRequest<T> discord(Discord discord) {
        this.discord = (DiscordInternal) discord;
        return this;
    }

    public WebRequest<T> endpoint(String endpoint) {
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

    public CompletableFuture<JsonNode> execute(String token) {
        return request(token, endpoint, method, node);
    }

    @SuppressWarnings("SameParameterValue")
    private CompletableFuture<JsonNode> request(
            String prefixedToken,
            String location,
            Method method,
            JsonNode node) {
        CompletableFuture<JsonNode> future = new CompletableFuture<>();
        discord.getThreadPool()
                .execute(() -> {
                    try {
                        String s = node.toString();
                        logger.trace("Creating " + method.getDescriptor() + " Request to " + BASE_URL + location + " with body: " + s);
                        HttpResponse<String> response = CLIENT.send(HttpRequest
                                        .newBuilder()
                                        .uri(URI.create(BASE_URL + location))
                                        .headers("User-Agent", "DiscordBot (http://kaleidox.de, 0.1)",
                                                "Content-Type", "application/json",
                                                "Authorization", prefixedToken)
                                        .method(method.getDescriptor(),
                                                HttpRequest.BodyPublishers.ofString(s))
                                        .build(),
                                HttpResponse.BodyHandlers.ofString());
                        logger.trace("Recieved status code " + response.statusCode() + " from Discord with body: " + response.body());
                        boolean dispatch = ResponseDispatch.dispatch(response, future);
                        if (!dispatch & (future.isCancelled() || future.isCompletedExceptionally())) {
                            logger.error("Something went horribly wrong in WebRequest.java, please contact the developer.");
                        } else {
                            if (dispatch) {
                                future.complete(JsonHelper.parse(response.body()));
                            }
                        }
                        future.completeExceptionally(new UnknownError("An unknown error ocurred."));
                    } catch (IOException | InterruptedException e) {
                        future.completeExceptionally(e);
                    }
                });

        return future;
    }
}

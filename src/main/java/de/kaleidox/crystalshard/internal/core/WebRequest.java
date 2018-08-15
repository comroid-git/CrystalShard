package de.kaleidox.crystalshard.internal.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.kaleidox.logging.Logger;
import de.kaleidox.util.JsonHelper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

public class WebRequest<T> {
    private static final Logger logger = new Logger(WebRequest.class);
    private static final String BASE_URL = "https://discordapp.com/api";
    private static final HttpClient CLIENT = HttpClient.newHttpClient();

    public WebRequest() {
    }

    public void execute(String token) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.set("content", JsonHelper.nodeOf("Hello World!"));
        node.set("file", JsonHelper.nodeOf("content"));

        request("Bot "+token, "/channels/479438264491573249/messages", Method.POST, node)
                .thenAcceptAsync(System.out::println);
    }

    @SuppressWarnings("SameParameterValue")
    private CompletableFuture<JsonNode> request(
            String prefixedToken,
            String location,
            Method method,
            JsonNode node) {
        CompletableFuture<JsonNode> future = new CompletableFuture<>();
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
            future.complete(JsonHelper.parse(response.body()));
        } catch (IOException | InterruptedException e) {
            future.completeExceptionally(e);
        }

        return future;
    }
}

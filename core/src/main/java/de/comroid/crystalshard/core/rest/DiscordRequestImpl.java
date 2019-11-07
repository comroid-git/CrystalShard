package de.comroid.crystalshard.core.rest;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.logging.Level;

import de.comroid.crystalshard.api.Discord;
import de.comroid.crystalshard.core.api.rest.DiscordEndpoint;
import de.comroid.crystalshard.core.api.rest.DiscordRequest;
import de.comroid.crystalshard.core.api.rest.HTTPStatusCodes;
import de.comroid.crystalshard.core.api.rest.RestMethod;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.flogger.FluentLogger;

import static de.comroid.crystalshard.CrystalShard.URL;
import static de.comroid.crystalshard.CrystalShard.VERSION;

public class DiscordRequestImpl<T> implements DiscordRequest<T> {
    private final static FluentLogger log = FluentLogger.forEnclosingClass();
    private final static HttpClient client;

    private final Discord api;
    private HttpRequest.Builder request;
    private RestMethod method;
    private HttpRequest.BodyPublisher publisher;
    private int expectedCode = 200;
    private DiscordEndpoint endpoint;
    private Object[] endpointArgs;

    static {
        client = HttpClient.newBuilder()
                .build();
    }

    public DiscordRequestImpl(Discord api) {
        this.api = api;

        this.request = HttpRequest.newBuilder()
                .header("Authorization", "Bot " + api.getToken())
                .header("User-Agent", "DiscordBot (" + URL + ", " + VERSION + ") using CrystalShard");
    }

    @Override
    public DiscordRequest<T> method(RestMethod method) {
        this.method = method;

        return this;
    }

    @Override
    public DiscordRequest<T> header(String name, String value) {
        request.header(name, value);

        return this;
    }

    @Override
    public DiscordRequest<T> body(String body) {
        this.publisher = HttpRequest.BodyPublishers.ofString(body);

        return this;
    }

    @Override
    public DiscordRequest<T> expectCode(int code) {
        this.expectedCode = code;

        return this;
    }

    @Override
    public CompletableFuture<T> executeAs(Function<JSONObject, T> mapper) {
        final HttpRequest request = this.request.uri(endpoint.uri(endpointArgs))
                .method(method.toString(), publisher)
                .build();
        final CompletableFuture<T> future = new CompletableFuture<>();

        try {
            api.getRatelimiter().submit(endpoint, () -> {
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() != expectedCode) log.at(Level.WARNING)
                        .log("Received unexpected status code: " + HTTPStatusCodes.toString(response.statusCode()));

                return response;
            })
                    .thenApply(HttpResponse::body)
                    .thenApply(JSON::parseObject)
                    .thenApply(mapper)
                    .thenAccept(future::complete);
        } catch (Exception e) {
            future.completeExceptionally(new RuntimeException("Unexpected Exception", e));
        }

        return future;
    }

    @Override
    public DiscordRequest<T> endpoint(DiscordEndpoint endpoint, Object... args) throws IllegalArgumentException {
        this.endpoint = endpoint;
        this.endpointArgs = args;

        return this;
    }

    @Override
    public DiscordEndpoint getEndpoint() {
        return endpoint;
    }

    @Override
    public Discord getAPI() {
        return api;
    }
}
package de.kaleidox.crystalshard.core.net.request;

import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.Nullable;

import de.kaleidox.crystalshard.Injector;
import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.core.net.request.endpoint.RequestURI;
import de.kaleidox.util.helpers.JsonHelper;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public interface WebRequest<T> {
    WebRequest<T> addHeader(String name, String value);

    RequestURI getUri();

    WebRequest<T> setUri(RequestURI uri);

    HttpMethod getMethod();

    WebRequest<T> setMethod(HttpMethod method);

    JsonNode getNode();

    WebRequest<T> setNode(JsonNode node);

    WebRequest<T> setNode(Object... data);

    default CompletableFuture<Void> executeAsVoid() {
        return execute().thenApply(n -> null);
    }

    CompletableFuture<String> execute() throws RuntimeException;

    default CompletableFuture<T> executeAs(Function<JsonNode, T> mapper) {
        return executeAsNode().thenApply(mapper);
    }

    default CompletableFuture<JsonNode> executeAsNode() {
        return execute().thenApply(JsonHelper::parse);
    }

    @SuppressWarnings("unchecked")
    static <T> WebRequest<T> create(@Nullable Discord discord) {
        //TODO test the generic creation
        return (WebRequest<T>) (discord != null
                ? Injector.create(WebRequest.class, discord)
                : Injector.create(WebRequest.class)
        );
    }
}

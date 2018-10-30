package de.kaleidox.crystalshard.core.net.request;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.core.net.request.endpoint.RequestURI;
import de.kaleidox.crystalshard.util.helpers.JsonHelper;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public interface WebRequest<T> {
    static <T> WebRequest<T> create() {
        return null; // TODO: 30.10.2018
    }

    WebRequest<T> addHeader(String name, String value);

    WebRequest<T> setNode(JsonNode node);

    RequestURI getUri();

    WebRequest<T> setUri(RequestURI uri);

    HttpMethod getMethod();

    WebRequest<T> setMethod(HttpMethod method);

    JsonNode getNode();

    WebRequest<T> setNode(Object... data);

    CompletableFuture<String> execute() throws RuntimeException;

    default CompletableFuture<JsonNode> executeAsNode() {
        return execute().thenApply(JsonHelper::parse);
    }

    default CompletableFuture<Void> executeAsVoid() {
        return execute().thenApply(n -> null);
    }

    default CompletableFuture<T> executeAs(Function<JsonNode, T> mapper) {
        return executeAsNode().thenApply(mapper);
    }
}

package de.kaleidox.crystalshard.core.net.request;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.core.net.request.endpoint.RequestURI;
import de.kaleidox.crystalshard.util.helpers.JsonHelper;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public interface WebRequest<T> {
    WebRequest<T> addHeader(String name, String value);

    WebRequest<T> setUri(RequestURI uri);

    WebRequest<T> setMethod(HttpMethod method);

    WebRequest<T> setNode(JsonNode node);

    WebRequest<T> setNode(Object... data);

    RequestURI getUri();

    HttpMethod getMethod();

    JsonNode getNode();

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

    static <T> WebRequest<T> create() {
        return null; // TODO: 30.10.2018
    }
}

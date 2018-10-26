package de.kaleidox.crystalshard.core.net.request;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public interface WebRequest<T> {
    WebRequest<T> endpoint(Endpoint endpoint);
    
    WebRequest<T> node(JsonNode node);
    
    WebRequest<T> node(Object... data);
    
    WebRequest<T> method(Method method);
    
    Endpoint getEndpoint();
    
    JsonNode getNode();
    
    Method getMethod();
    
    CompletableFuture<JsonNode> execute();
    
    CompletableFuture<T> execute(Function<JsonNode, T> mapper);
    
    CompletableFuture<Void> executeNull();
}

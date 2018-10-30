package de.kaleidox.crystalshard.core.net.request;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.core.net.request.endpoint.RequestURI;
import de.kaleidox.crystalshard.logging.Logger;
import de.kaleidox.crystalshard.util.helpers.JsonHelper;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class WebRequestImpl<T> implements WebRequest<T> {
    protected static final Logger logger = new Logger(WebRequest.class);
    protected static final HttpClient CLIENT = HttpClient.newHttpClient();
    protected final HttpRequest.Builder requestBuilder;
    protected final CompletableFuture<String> future;
    protected RequestURI uri;
    protected HttpMethod method;
    protected JsonNode node;

    public WebRequestImpl() {
        this.requestBuilder = HttpRequest.newBuilder();
        this.future = new CompletableFuture<>();
    }

    @Override
    public WebRequest<T> addHeader(String name, String value) {
        requestBuilder.header(name, value);
        return this;
    }

    @Override
    public WebRequest<T> setUri(RequestURI uri) {
        this.uri = uri;
        return this;
    }

    @Override
    public WebRequest<T> setMethod(HttpMethod method) {
        this.method = method;
        return this;
    }

    @Override
    public WebRequest<T> setNode(JsonNode node) {
        this.node = node;
        return this;
    }

    @Override
    public WebRequest<T> setNode(Object... data) {
        this.node = JsonHelper.objectNode(data);
        return this;
    }

    @Override
    public RequestURI getUri() {
        return uri;
    }

    @Override
    public HttpMethod getMethod() {
        return method;
    }

    @Override
    public JsonNode getNode() {
        return node;
    }

    @Override
    public CompletableFuture<String> execute() throws RuntimeException {
        Objects.requireNonNull(uri, "URI is not set!");
        Objects.requireNonNull(method, "Method is not set!");
        Objects.requireNonNull(node, "Node is not set!");

        try {
            HttpResponse<String> response = CLIENT.send(requestBuilder.uri(uri.getURI())
                    .method(method.getDescriptor(), HttpRequest.BodyPublishers.ofString(method == HttpMethod.GET ? "" : node.toString()))
                    .build(), HttpResponse.BodyHandlers.ofString());
            future.complete(response.body());
            return future;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

package de.kaleidox.crystalshard.core.net.request;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.core.net.request.endpoint.RequestURI;
import de.kaleidox.crystalshard.logging.Logger;
import de.kaleidox.util.helpers.JsonHelper;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class WebRequestImpl<T> implements WebRequest<T> {
    protected static final Logger logger = new Logger(WebRequest.class);
    protected static final OkHttpClient CLIENT = new OkHttpClient();
    protected final Request.Builder requestBuilder;
    protected final CompletableFuture<String> future;
    protected RequestURI uri;
    protected HttpMethod method;
    protected JsonNode node;

    public WebRequestImpl() {
        logger.warn("The Java 8 WebRequest implementation has not been tested yet."); // TODO: 31.10.2018 Test
        this.requestBuilder = new Request.Builder();
        this.future = new CompletableFuture<>();
    }

    @Override
    public WebRequest<T> addHeader(String name, String value) {
        requestBuilder.addHeader(name, value);
        return this;
    }

    @Override
    public WebRequest<T> setNode(JsonNode node) {
        this.node = node;
        return this;
    }

    @Override
    public RequestURI getUri() {
        return uri;
    }

    @Override
    public WebRequest<T> setUri(RequestURI uri) {
        this.uri = uri;
        return this;
    }

    @Override
    public HttpMethod getMethod() {
        return method;
    }

    @Override
    public WebRequest<T> setMethod(HttpMethod method) {
        this.method = method;
        return this;
    }

    @Override
    public JsonNode getNode() {
        return node;
    }

    @Override
    public WebRequest<T> setNode(Object... data) {
        this.node = JsonHelper.objectNode(data);
        return this;
    }

    @Override
    public CompletableFuture<String> execute() throws RuntimeException {
        Objects.requireNonNull(uri, "URI is not set!");
        Objects.requireNonNull(method, "Method is not set!");
        Objects.requireNonNull(node, "Node is not set!");

        try {
            Request request = requestBuilder.url(uri.getURI().toString())
                    .method(method.getDescriptor(),
                            RequestBody.create(MediaType.parse("application/json; charset=utf-8"), node.toString()))
                    .build();
            Response response = CLIENT.newCall(request).execute();
            assert response.body() != null : "Unexcpected NullPointer in WebRequestImpl";
            future.complete(response.body().string());
            return future;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

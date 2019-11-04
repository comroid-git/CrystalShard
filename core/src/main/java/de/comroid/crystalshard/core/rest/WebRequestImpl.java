package de.comroid.crystalshard.core.rest;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.logging.Level;

import de.comroid.crystalshard.core.api.rest.HTTPStatusCodes;
import de.comroid.crystalshard.core.api.rest.RestMethod;
import de.comroid.crystalshard.core.api.rest.WebRequest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.flogger.FluentLogger;

public class WebRequestImpl<T> implements WebRequest<T> {
    private final static FluentLogger log = FluentLogger.forEnclosingClass();
    private final static HttpClient client;

    private HttpRequest.Builder request;
    private RestMethod method;
    private HttpRequest.BodyPublisher publisher;
    private int expectedCode = 200;

    static {
        client = HttpClient.newBuilder()
                .build();
    }

    public WebRequestImpl() {
        this.request = HttpRequest.newBuilder();
    }

    @Override
    public WebRequest<T> method(RestMethod method) {
        this.method = method;

        return this;
    }

    @Override
    public WebRequest<T> header(String name, String value) {
        request.header(name, value);

        return this;
    }

    @Override
    public WebRequest<T> uri(URI uri) {
        request.uri(uri);

        return this;
    }

    @Override
    public WebRequest<T> body(String body) {
        this.publisher = HttpRequest.BodyPublishers.ofString(body);

        return this;
    }

    @Override
    public WebRequest<T> expectCode(int code) {
        this.expectedCode = code;

        return this;
    }

    @Override
    public CompletableFuture<T> executeAs(Function<JSONObject, T> mapper) {
        return client.sendAsync(
                request.method(method.toString(), publisher).build(),
                HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    if (response.statusCode() != expectedCode)
                        log.at(Level.WARNING).log("Received unexpected status code: "
                                + HTTPStatusCodes.toString(response.statusCode()));

                    return response.body();
                })
                .thenApply(JSON::parseObject)
                .thenApply(mapper);
    }
}

package org.comroid.crystalshard;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.comroid.mutatio.span.Span;
import org.comroid.restless.CommonHeaderNames;
import org.comroid.restless.REST;
import org.comroid.restless.body.BodyBuilderType;
import org.comroid.restless.endpoint.CompleteEndpoint;
import org.comroid.uniform.model.Serializable;
import org.comroid.uniform.node.UniNode;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.container.DataContainer;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.comroid.restless.HTTPStatusCodes.NO_CONTENT;
import static org.comroid.restless.HTTPStatusCodes.OK;

public interface DiscordREST extends Context {
    @Internal
    Logger logger = LogManager.getLogger();

    @NotNull
    @Internal
    static REST.Header.List createHeaders(String token) {
        REST.Header.List headers = new REST.Header.List();

        if (token != null)
            headers.add(CommonHeaderNames.AUTHORIZATION, "Bot " + token);
        headers.add(CommonHeaderNames.USER_AGENT, String.format(
                "DiscordBot (%s, %s) %s",
                CrystalShard.URL,
                CrystalShard.VERSION.toSimpleString(),
                CrystalShard.VERSION)
        );
        return headers;
    }

    @Internal
    static <R> Function<Throwable, R> getExceptionLogger(DiscordREST it, REST.Method method, CompleteEndpoint endpoint) {
        return it.getAPI().exceptionLogger(
                logger,
                Level.ERROR,
                String.format("%s-Request @ %s", method, endpoint.getSpec()),
                false
        );
    }

    @Internal
    default CompletableFuture<UniNode> newRequest(
            REST.Method method,
            CompleteEndpoint endpoint
    ) {
        return newRequest(method, endpoint, null, Function.identity());
    }

    @Internal
    default <N extends UniNode> CompletableFuture<N> newRequest(
            REST.Method method,
            CompleteEndpoint endpoint,
            Function<UniNode, N> dataResolver
    ) {
        return newRequest(method, endpoint, null, dataResolver);
    }

    @Internal
    default <R extends DataContainer<? super R>> CompletableFuture<R> newRequest(
            REST.Method method,
            CompleteEndpoint endpoint,
            GroupBind<R> responseType
    ) {
        return newRequest(method, endpoint, null, responseType);
    }

    // actual callers

    @Internal
    default <R extends DataContainer<? super R>, N extends UniNode> CompletableFuture<R> newRequest(
            REST.Method method,
            CompleteEndpoint endpoint,
            BodyBuilderType<N> type,
            Consumer<N> builder,
            GroupBind<R> responseType
    ) {
        return newRequest(method, endpoint, buildBody(type, builder), responseType);
    }

    @Internal
    default <T extends DataContainer<? super T>> CompletableFuture<@Nullable T> newRequest(
            REST.Method method,
            CompleteEndpoint endpoint,
            Serializable body,
            GroupBind<T> responseType
    ) {
        return newRequest(method, endpoint, body, responseType, Span::get);
    }

    @Internal
    default <T extends DataContainer<? super T>, R> CompletableFuture<R> newRequest(
            REST.Method method,
            CompleteEndpoint endpoint,
            Serializable body,
            GroupBind<T> responseType,
            Function<Span<T>, R> spanResolver
    ) {
        return getREST().request(responseType)
                .method(method)
                .endpoint(endpoint)
                .body(body)
                .addHeaders(createHeaders(as(Bot.class).map(Bot::getToken).orElse(null)))
                .execute$deserialize()
                .thenApply(spanResolver)
                .exceptionally(getExceptionLogger(this, method, endpoint));
    }

    // helpers

    @Internal
    default CompletableFuture<UniNode> newRequest(
            REST.Method method,
            CompleteEndpoint endpoint,
            Serializable body
    ) {
        return newRequest(method, endpoint, body, Function.identity());
    }

    @Internal
    default <R> CompletableFuture<R> newRequest(
            REST.Method method,
            CompleteEndpoint endpoint,
            Serializable body,
            Function<UniNode, R> dataResolver
    ) {
        return getREST().request()
                .method(method)
                .endpoint(endpoint)
                .body(body)
                .addHeaders(createHeaders(as(Bot.class).map(Bot::getToken).orElse(null)))
                .expect(true, OK, NO_CONTENT)
                .execute$body()
                .thenApply(Serializable::toUniNode)
                .thenApply(dataResolver)
                .exceptionally(getExceptionLogger(this, method, endpoint));
    }

    @Internal
    default <N extends UniNode> N buildBody(BodyBuilderType<N> type, Consumer<N> builder) {
        final N body = type.apply(getSerializer());
        builder.accept(body);
        return body;
    }
}

package org.comroid.crystalshard;

import org.comroid.api.Polyfill;
import org.comroid.crystalshard.entity.user.User;
import org.comroid.crystalshard.gateway.event.GatewayEvent;
import org.comroid.crystalshard.gateway.presence.OwnPresence;
import org.comroid.crystalshard.model.presence.Activity;
import org.comroid.crystalshard.model.presence.UserStatus;
import org.comroid.mutatio.pipe.Pipe;
import org.comroid.mutatio.span.Span;
import org.comroid.restless.REST;
import org.comroid.restless.body.BodyBuilderType;
import org.comroid.restless.endpoint.CompleteEndpoint;
import org.comroid.uniform.node.UniNode;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.container.DataContainer;
import org.jetbrains.annotations.ApiStatus.Internal;

import java.io.Closeable;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;

public interface Bot extends Context, Closeable {
    default long getOwnID() {
        return getYourself().getID();
    }

    Pipe<? extends GatewayEvent> getEventPipeline();

    boolean isReady();

    User getYourself();

    int getCurrentShardID();

    int getShardCount();

    OwnPresence getOwnPresence();

    default CompletableFuture<Void> updateStatus(UserStatus status) {
        return updatePresence(status, null, null);
    }

    default CompletableFuture<Void> updateActivity(Activity.Type type, String detail) {
        return updatePresence(null, type, detail);
    }

    default CompletableFuture<Void> updatePresence(UserStatus status, String detail) {
        return updatePresence(status, Activity.Type.PLAYING, detail);
    }

    default CompletableFuture<Void> updatePresence(UserStatus status, Activity.Type type, String detail) {
        OwnPresence presence = getOwnPresence();
        if (status != null)
            presence.setStatus(status);
        if (type != null)
            presence.addActivity(new Activity(this, type, detail));
        return presence.update();
    }

    @Internal
    default CompletableFuture<UniNode> newRequest(
            REST.Method method,
            CompleteEndpoint endpoint
    ) {
        return Polyfill.uncheckedCast(newRequest(method, endpoint, null));
    }

    @Internal
    default <R extends DataContainer<? super R>> CompletableFuture<R> newRequest(
            REST.Method method,
            CompleteEndpoint endpoint,
            GroupBind<R> responseType
    ) {
        return newRequest(method, endpoint, (UniNode) null, responseType);
    }

    @Internal
    default <R extends DataContainer<? super R>, N extends UniNode> CompletableFuture<R> newRequest(
            REST.Method method,
            CompleteEndpoint endpoint,
            BodyBuilderType<N> type,
            Consumer<N> builder,
            GroupBind<R> responseType
    ) {
        N data = type.apply(getSerializer());
        builder.accept(data);
        return newRequest(method, endpoint, data, responseType);
    }

    @Internal
    default <R extends DataContainer<? super R>, N extends UniNode> CompletableFuture<R> newRequest(
            REST.Method method,
            CompleteEndpoint endpoint,
            DataContainer<?> body,
            GroupBind<R> responseType
    ) {
        return newRequest(method, endpoint, body.toObjectNode(getSerializer()), responseType);
    }

    @Internal
    default <T extends DataContainer<? super T>, N extends UniNode> CompletableFuture<T> newRequest(
            REST.Method method,
            CompleteEndpoint endpoint,
            N body,
            GroupBind<T> responseType
    ) {
        return newRequest(method, endpoint, body, responseType, Span::get);
    }

    @Internal
    <T extends DataContainer<? super T>, R, N extends UniNode> CompletableFuture<R> newRequest(
            REST.Method method,
            CompleteEndpoint endpoint,
            N body,
            GroupBind<T> responseType,
            Function<Span<T>, R> spanResolver
    );

    String getToken();
}

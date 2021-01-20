package org.comroid.crystalshard;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.entity.SnowflakeCache;
import org.comroid.crystalshard.entity.user.User;
import org.comroid.crystalshard.gateway.event.GatewayEvent;
import org.comroid.crystalshard.gateway.presence.OwnPresence;
import org.comroid.crystalshard.model.presence.Activity;
import org.comroid.crystalshard.model.presence.UserStatus;
import org.comroid.mutatio.pipe.Pipe;
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

public interface Bot extends ContextualProvider.Underlying, Closeable {
    SnowflakeCache getSnowflakeCache();

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
    default <R extends DataContainer<? super R>, N extends UniNode> CompletableFuture<R> newRequest(
            REST.Method method,
            CompleteEndpoint endpoint,
            GroupBind<R> responseType
    ) {
        return newRequest(method, endpoint, responseType, null, null);
    }

    @Internal
    <R extends DataContainer<? super R>, N extends UniNode> CompletableFuture<R> newRequest(
            REST.Method method,
            CompleteEndpoint endpoint,
            GroupBind<R> responseType,
            BodyBuilderType<N> type,
            Consumer<N> builder
    );

    String getToken();
}

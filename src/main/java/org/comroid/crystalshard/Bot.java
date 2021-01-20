package org.comroid.crystalshard;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.entity.SnowflakeCache;
import org.comroid.crystalshard.entity.user.User;
import org.comroid.crystalshard.gateway.event.GatewayEvent;
import org.comroid.crystalshard.gateway.presence.StatusUpdater;
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

    default StatusUpdater updateOwnPresence() {
        return new StatusUpdater(this);
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

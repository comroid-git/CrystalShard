package org.comroid.crystalshard;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.entity.user.User;
import org.comroid.crystalshard.gateway.event.GatewayEvent;
import org.comroid.crystalshard.rest.Endpoint;
import org.comroid.crystalshard.rest.response.AbstractRestResponse;
import org.comroid.mutatio.pipe.Pipe;
import org.comroid.restless.REST;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.ApiStatus.Internal;

import java.io.Closeable;
import java.util.concurrent.CompletableFuture;

public interface Bot extends ContextualProvider.Underlying, Closeable {
    SnowflakeCache getSnowflakeCache();

    Pipe<? extends GatewayEvent> getEventPipeline();

    boolean isReady();

    User getYourself();

    int getCurrentShardID();

    int getShardCount();

    @Internal
    <R extends AbstractRestResponse> CompletableFuture<R> newRequest(REST.Method method, Endpoint<R> endpoint);
}

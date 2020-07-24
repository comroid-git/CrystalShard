package org.comroid.crystalshard;

import org.comroid.crystalshard.core.gateway.Gateway;
import org.comroid.mutatio.ref.FutureReference;
import org.comroid.restless.HttpAdapter;
import org.comroid.restless.REST;
import org.comroid.uniform.SerializationAdapter;

import java.net.URI;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledExecutorService;

public final class CrystalShard {
    public static final ThreadGroup THREAD_GROUP = new ThreadGroup("CrystalShard");
    public static HttpAdapter HTTP_ADAPTER;
    public static SerializationAdapter<?, ?, ?> SERIALIZATION_ADAPTER;

    public static CompletableFuture<Gateway> createGateway(
            FutureReference<DiscordBot> botRef,
            ScheduledExecutorService executorService,
            URI webSocketURI,
            REST.Header.List socketHeaders
    ) {
        return HTTP_ADAPTER.createWebSocket(SERIALIZATION_ADAPTER, executorService, webSocketURI, socketHeaders)
                .thenApply(ws -> new Gateway(botRef, executorService, ws));
    }
}

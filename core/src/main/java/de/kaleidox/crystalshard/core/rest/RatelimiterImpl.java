package de.kaleidox.crystalshard.core.rest;

import java.net.http.HttpResponse;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.core.api.rest.DiscordEndpoint;
import de.kaleidox.crystalshard.core.api.rest.Ratelimiter;

public class RatelimiterImpl implements Ratelimiter {
    private final Discord api;

    public RatelimiterImpl(Discord api) {
        this.api = api;
    }

    @Override
    public CompletableFuture<HttpResponse<String>> submit(
            DiscordEndpoint endpoint,
            Callable<HttpResponse<String>> execution
    ) { // todo
        CompletableFuture<HttpResponse<String>> future = new CompletableFuture<>();

        try {
            execution.call();
        } catch (Exception e) {
            future.completeExceptionally(new RuntimeException("Exception in Ratelimit-Callable", e));
        }

        return future;
    }

    @Override
    public Discord getAPI() {
        return api;
    }
}

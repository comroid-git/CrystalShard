package org.comroid.crystalshard.core.rest;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

import org.comroid.crystalshard.api.model.ApiBound;
import org.comroid.uniform.http.REST;

public interface Ratelimiter extends ApiBound {
    CompletableFuture<REST.Response> submit(
            DiscordEndpoint endpoint,
            Callable<REST.Response> execution
    );
}

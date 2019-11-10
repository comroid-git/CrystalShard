package de.comroid.crystalshard.core.rest;

import java.net.http.HttpResponse;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

import de.comroid.crystalshard.api.model.ApiBound;

public interface Ratelimiter extends ApiBound {
    CompletableFuture<HttpResponse<String>> submit(
            DiscordEndpoint endpoint,
            Callable<HttpResponse<String>> execution
    );
}

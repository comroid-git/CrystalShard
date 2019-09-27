package de.kaleidox.crystalshard.core.api.rest;

import java.net.http.HttpResponse;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

import de.kaleidox.crystalshard.api.model.ApiBound;

public interface Ratelimiter extends ApiBound {
    CompletableFuture<HttpResponse<String>> submit(
            DiscordEndpoint endpoint,
            Callable<HttpResponse<String>> execution
    );
}

package de.kaleidox.crystalshard.core.api.rest;

import java.util.concurrent.CompletableFuture;

public interface Ratelimiter {
    <T> CompletableFuture<T> submit(DiscordRequest<T> request);
}

package de.kaleidox.crystalshard.core.net.request.ratelimiting;

import de.kaleidox.crystalshard.core.concurrent.ThreadPool;
import de.kaleidox.crystalshard.core.net.request.Endpoint;
import de.kaleidox.crystalshard.core.net.request.WebRequest;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.logging.Logger;

import java.net.http.HttpHeaders;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Ratelimiting {
    private final static Logger logger = new Logger(Ratelimiting.class);
    final ThreadPool executePool;
    @SuppressWarnings("ALL")
    private final DiscordInternal discord;
    private final BucketManager bucketManager;
    private final ConcurrentHashMap<Endpoint, AtomicInteger> remainingMap;
    private final ConcurrentHashMap<Endpoint, AtomicInteger> limitMap;
    private final ConcurrentHashMap<Endpoint, AtomicReference<Instant>> resetMap;

    /**
     * Creates a new Ratelimiter Instance.
     *
     * @param discord The discord object to attach the ratelimiter to.
     */
    public Ratelimiting(DiscordInternal discord) {
        this.discord = discord;
        this.executePool = new ThreadPool(discord, -1, "Ratelimit Execution");
        this.bucketManager = new BucketManager(discord, this);
        this.remainingMap = new ConcurrentHashMap<>();
        this.limitMap = new ConcurrentHashMap<>();
        this.resetMap = new ConcurrentHashMap<>();
    }

    public AtomicInteger getRemaining(Endpoint endpoint) {
        assureAtomicValues(endpoint);
        AtomicInteger remaining = remainingMap.get(endpoint);
        AtomicReference<Instant> reset = resetMap.get(endpoint);
        if (remaining.get() == 0 && reset.get().isAfter(Instant.now())) {
            remaining.incrementAndGet();
        }
        return remaining;
    }

    public AtomicInteger getLimit(Endpoint endpoint) {
        assureAtomicValues(endpoint);
        return limitMap.get(endpoint);
    }

    public AtomicReference<Instant> getReset(Endpoint endpoint) {
        assureAtomicValues(endpoint);
        return resetMap.get(endpoint);
    }

    /**
     * Schedules a Request to be sent at the next available slot.
     * Use {@link WebRequest} to execute a request.
     *
     * @param request       The request to send. Used to gather information like Endpoints.
     * @param headersFuture A CompletableFuture that completes with the HttpHeaders of the request.
     * @param requestTask   A runnable to execute the actual request.
     * @param <T>           Type variable for the request.
     */
    public <T> void schedule(WebRequest<T> request,
                             CompletableFuture<HttpHeaders> headersFuture,
                             Runnable requestTask) {
        Endpoint endpoint = request.getEndpoint();

        headersFuture.thenAcceptAsync(headers -> {
            try {
                // set fail-safe map elements, if not set for this endpoint
                headers.firstValue("X-RateLimit-Remaining")
                        .map(Integer::parseInt)
                        .ifPresent(readyAt -> remainingMap.get(endpoint).set(readyAt));
                headers.firstValue("X-RateLimit-Limit")
                        .map(Integer::parseInt)
                        .ifPresent(limit -> limitMap.get(endpoint).set(limit));
                headers.firstValue("X-RateLimit-Reset")
                        .map(Long::parseLong)
                        .map(Instant::ofEpochMilli)
                        .ifPresent(retryAt -> resetMap.get(endpoint).set(retryAt));
            } catch (NullPointerException e) {
                logger.exception(e, "NPE on Ratelimit header evaluation.");
            }
        });

        bucketManager.schedule(endpoint, requestTask);
    }

    private void assureAtomicValues(Endpoint endpoint) {
        remainingMap.putIfAbsent(endpoint, new AtomicInteger(1));
        limitMap.putIfAbsent(endpoint, new AtomicInteger(1));
        resetMap.putIfAbsent(endpoint, new AtomicReference<>(Instant.now().minusSeconds(1)));
    }
}

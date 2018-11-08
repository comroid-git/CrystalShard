package de.kaleidox.crystalshard.core.net.request.ratelimit;

import de.kaleidox.crystalshard.core.concurrent.ThreadPoolImpl;
import de.kaleidox.crystalshard.core.net.request.WebRequest;
import de.kaleidox.crystalshard.core.net.request.endpoint.RequestURI;
import de.kaleidox.crystalshard.core.net.request.ratelimiting.Ratelimiter;
import de.kaleidox.crystalshard.logging.Logger;
import de.kaleidox.crystalshard.main.Discord;
import java.net.http.HttpHeaders;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class RatelimiterImpl implements Ratelimiter {
    private final static Logger logger = new Logger(RatelimiterImpl.class);
    @SuppressWarnings("ALL")
    private final Discord discord;
    private final BucketManager bucketManager;
    private final ConcurrentHashMap<RequestURI, AtomicInteger> remainingMap;
    private final ConcurrentHashMap<RequestURI, AtomicInteger> limitMap;
    private final ConcurrentHashMap<RequestURI, AtomicReference<Instant>> resetMap;
    final ThreadPoolImpl executePool;

    /**
     * Creates a new Ratelimiter Instance.
     *
     * @param discord The discord object to attach the ratelimiter to.
     */
    public RatelimiterImpl(Discord discord) {
        this.discord = discord;
        this.executePool = new ThreadPoolImpl(discord, -1, "Ratelimit Execution");
        this.bucketManager = new BucketManager(discord, this);
        this.remainingMap = new ConcurrentHashMap<>();
        this.limitMap = new ConcurrentHashMap<>();
        this.resetMap = new ConcurrentHashMap<>();
    }

    public AtomicInteger getRemaining(RequestURI discordRequestURI) {
        assureAtomicValues(discordRequestURI);
        AtomicInteger remaining = remainingMap.get(discordRequestURI);
        AtomicReference<Instant> reset = resetMap.get(discordRequestURI);
        if (remaining.get() == 0 && reset.get()
                .isAfter(Instant.now())) {
            remaining.incrementAndGet();
        }
        return remaining;
    }

    private void assureAtomicValues(RequestURI discordRequestURI) {
        remainingMap.putIfAbsent(discordRequestURI, new AtomicInteger(1));
        limitMap.putIfAbsent(discordRequestURI, new AtomicInteger(1));
        resetMap.putIfAbsent(discordRequestURI, new AtomicReference<>(Instant.now()
                .minusSeconds(1)));
    }

    public AtomicInteger getLimit(RequestURI discordRequestURI) {
        assureAtomicValues(discordRequestURI);
        return limitMap.get(discordRequestURI);
    }

    public AtomicReference<Instant> getReset(RequestURI discordRequestURI) {
        assureAtomicValues(discordRequestURI);
        return resetMap.get(discordRequestURI);
    }

    /**
     * Schedules a Request to be sent at the next available slot. Use {@link WebRequest} to execute a request.
     *
     * @param request       The request to send. Used to gather information like Endpoints.
     * @param headersFuture A CompletableFuture that completes with the HttpHeaders of the request.
     * @param requestTask   A runnable to execute the actual request.
     * @param <T>           Type variable for the request.
     */
    public <T> void schedule(WebRequest<T> request, CompletableFuture<HttpHeaders> headersFuture, Runnable requestTask) {
        RequestURI requestUri = request.getUri();

        headersFuture.thenAcceptAsync(headers -> {
            try {
                // set fail-safe map elements, if not set for this endpoint
                headers.firstValue("X-RateLimit-Remaining")
                        .map(Integer::parseInt)
                        .ifPresent(readyAt -> remainingMap.get(requestUri)
                                .set(readyAt));
                headers.firstValue("X-RateLimit-Limit")
                        .map(Integer::parseInt)
                        .ifPresent(limit -> limitMap.get(requestUri)
                                .set(limit));
                headers.firstValue("X-RateLimit-Reset")
                        .map(Long::parseLong)
                        .map(Instant::ofEpochMilli)
                        .ifPresent(retryAt -> resetMap.get(requestUri)
                                .set(retryAt));
            } catch (NullPointerException e) {
                logger.exception(e, "NPE on Ratelimit header evaluation.");
            }
        });

        bucketManager.schedule(requestUri, requestTask);
    }
}

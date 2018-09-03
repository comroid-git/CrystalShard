package de.kaleidox.crystalshard.internal.core.net.request;

import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.core.concurrent.ThreadPool;
import de.kaleidox.logging.Logger;
import de.kaleidox.util.helpers.ListHelper;
import de.kaleidox.util.helpers.MapHelper;

import java.net.http.HttpHeaders;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static de.kaleidox.crystalshard.internal.core.net.request.Endpoint.*;

public class Ratelimiting {
    private final static Logger logger = new Logger(Ratelimiting.class);
    private final BucketQueue bucketList;
    private final ThreadPool bucketExecutionPool;
    private final DiscordInternal discord;

    public Ratelimiting(DiscordInternal discord) {
        this.discord = discord;
        this.bucketList = new BucketQueue();
        this.bucketExecutionPool = new ThreadPool(discord, -1, "Ratelimiter Execution");
        ThreadPool tickPool = new ThreadPool(discord, 1, "Ratelimiter Tick");

        tickPool.execute(() -> {
            //noinspection InfiniteLoopStatement
            while (true) {
                synchronized (bucketList) {
                    try {
                        bucketList.wait();
                        bucketList.assureAny();
                        boolean retry = true;
                        while (retry) {
                            retry = bucketList.getFirst().tryRun(); // block further actions until bucket could be sent
                        }
                        ListHelper.moveList(bucketList, -1, Bucket::new);
                    } catch (InterruptedException e) {
                        logger.exception(e, "RatelimitTickPool sleep interrupted.");
                    }
                }
            }
        });
    }

    void schedule(Endpoint endpoint, CompletableFuture<HttpHeaders> headersFuture, Runnable requestTask) {
        logger.deeptrace("Scheduling Request to endpoint " + endpoint.getUrl().toExternalForm());
        synchronized (bucketList) {
            bucketList.assureAny();
            while (!bucketList.getLast().addRequest(requestTask, endpoint)) {
                bucketList.append();
            }
            bucketList.notifyAll();
        }
        headersFuture.thenAcceptAsync(headers -> Block.getInstance(endpoint).update(headers), discord.getExecutor());
    }

    static class Block {
        private final static List<Block> instances = new ArrayList<>();
        private final Endpoint endpoint;
        private long originMilis;
        private long limit;
        private long remaining;
        private Instant reset;
        private Boolean global;
        private Runnable retryRunnable;

        Block(Endpoint endpoint, HttpHeaders headers) {
            this.endpoint = endpoint;
            if (headers != null) update(headers);
        }

        void attachRunnable(Runnable retryRunnable) {
            this.retryRunnable = retryRunnable;
        }

        void update(HttpHeaders headers) {
            this.originMilis = System.currentTimeMillis();
            try {
                this.limit = headers.firstValueAsLong("X-RateLimit-Limit").orElse(5);
                this.remaining = headers.firstValueAsLong("X-RateLimit-Remaining").orElse(0);
                this.reset = Instant.ofEpochMilli(headers.firstValueAsLong("X-RateLimit-Reset").orElse(0));
                this.global = headers.firstValue("X-RateLimit-Global").map(Boolean::valueOf).orElse(false);
            } catch (NullPointerException e) {
                logger.exception(e, "NPE on creating RatelimitingBlock for endpoint: " + endpoint);
                this.limit = 5;
                this.remaining = 0;
                this.reset = Instant.ofEpochMilli(0);
                this.global = false;
            }
            OptionalLong retryAfterOpt = headers.firstValueAsLong("Retry-After");
            if (retryAfterOpt.isPresent() && retryRunnable != null) {
                if (ThreadPool.isBotOwnThread()) {
                    ThreadPool.getThreadDiscord()
                            .getThreadPool()
                            .getScheduler()
                            .schedule(retryRunnable, retryAfterOpt.getAsLong(), TimeUnit.MILLISECONDS);
                } else {
                    logger.error("Could not reschedule ratelimited request " + endpoint + "; Thread does not belong to " +
                            "any Discord object. Please contact the Developer.");
                }
            }
        }

        boolean isReady() {
            return (originMilis + remaining) < System.currentTimeMillis();
        }

        Endpoint getEndpoint() {
            return endpoint;
        }

        static Block getInstance(Endpoint endpoint) {
            Optional<Block> complex = ListHelper.findComplex(instances, endpoint, Block::getEndpoint);
            if (complex.isPresent()) {
                return complex.get();
            } else {
                Block block = new Block(endpoint, null);
                instances.add(block);
                return block;
            }
        }
    }

    private class BucketQueue extends ArrayList<Bucket> {
        BucketQueue() {
            super();
        }

        void append() {
            add(new Bucket());
        }

        Bucket getFirst() {
            assureAt(0);
            return get(0);
        }

        Bucket getLast() {
            return get(size() - 1);
        }

        void assureAny() {
            if (size() == 0) {
                add(new Bucket());
            }
        }

        void assureAt(int index) {
            if (size() - 1 < index) {
                set(index, new Bucket());
            }
        }
    }

    private class Bucket {
        final ConcurrentHashMap<Block, Runnable> requests = new ConcurrentHashMap<>();
        final long birthTime;

        Bucket() {
            this.birthTime = System.currentTimeMillis();
        }

        boolean addRequest(Runnable request, Endpoint endpoint) {
            boolean couldAdd;

            if (MapHelper.containsKey(requests, endpoint, Block::getEndpoint) || requests.size() > 49) {
                couldAdd = false;
            } else {
                Block block;
                if (MapHelper.containsKey(RATELIMIT_COOLDOWNS, endpoint)) {
                    block = RATELIMIT_COOLDOWNS.get(endpoint);
                } else {
                    block = new Block(endpoint, null);
                    block.attachRunnable(request);
                }
                requests.put(block, request);
                couldAdd = true;
            }
            tryRun();

            return couldAdd;
        }

        boolean tryRun() {
            List<Block> collect = requests.entrySet()
                    .stream()
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
            if (ListHelper.booleanOfAll(collect, Block::isReady)) {
                runAll();
                return false;
            }
            return true;
        }

        @SuppressWarnings("SuspiciousMethodCalls")
        void runAll() {
            requests.forEach((key, value) -> {
                bucketExecutionPool.execute(value);
                requests.remove(key, value);
            });
        }
    }
}

package de.kaleidox.crystalshard.core.net.request.ratelimit;

import javax.naming.LimitExceededException;

import de.kaleidox.crystalshard.core.concurrent.ThreadPoolImpl;
import de.kaleidox.crystalshard.core.net.request.endpoint.DiscordRequestURI;
import de.kaleidox.crystalshard.core.net.request.endpoint.RequestURI;
import de.kaleidox.crystalshard.logging.Logger;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.util.functional.LivingInt;
import de.kaleidox.util.helpers.MapHelper;
import de.kaleidox.util.helpers.QueueHelper;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

class BucketManager {
    private final static Logger logger = new Logger(BucketManager.class);
    @SuppressWarnings("ALL")
    private final Discord discord;
    private final RatelimiterImpl ratelimiterImpl;
    private final ConcurrentLinkedQueue<Bucket> bucketQueue;
    private final ThreadPoolImpl atomicPool;
    private final LivingInt globalRatelimit;

    BucketManager(Discord discord, RatelimiterImpl ratelimiterImpl) {
        this.discord = discord;
        this.ratelimiterImpl = ratelimiterImpl;
        this.bucketQueue = new ConcurrentLinkedQueue<>();
        this.atomicPool = new ThreadPoolImpl(discord, 1, "BucketManager");
        this.globalRatelimit = new LivingInt(0, 0, -1, 20, TimeUnit.MILLISECONDS);

        cycle();
    }

    private void cycle() {
        atomicPool.execute(() -> {
            synchronized (bucketQueue) {
                //noinspection InfiniteLoopStatement
                while (true) {
                    try {
                        while (bucketQueue.isEmpty()) {
                            bucketQueue.wait();
                        }
                        Bucket poll = bucketQueue.poll();
                        while (!poll.canRun()) { // wait until the bucket can be run
                            logger.deeptrace("Ratelimited bucket " + poll + " for " + poll.waitDuration() + " MS");
                            Thread.sleep(poll.waitDuration());
                        }
                        poll.runAll();
                    } catch (InterruptedException e) {
                        logger.exception(e, "BucketQueue wait or sleep interrupted.");
                    }
                }
            }
        });
    }

    void schedule(RequestURI discordRequestURI, Runnable requestExecution) {
        synchronized (bucketQueue) {
            try {
                if (bucketQueue.isEmpty()) bucketQueue.add(new Bucket());
                boolean success = false;
                while (!success) {
                    Bucket poll = QueueHelper.getTail(bucketQueue);
                    assert poll != null;
                    if (poll.canAccept(discordRequestURI)) {
                        poll.addRequest(discordRequestURI, requestExecution);
                        success = true;
                    } else {
                        bucketQueue.add(new Bucket());
                    }
                }
                bucketQueue.notify();
            } catch (LimitExceededException e) {
                logger.exception(e);
            }
        }
    }

    private class Bucket {
        private ConcurrentHashMap<RequestURI, Runnable[]> requests;

        Bucket() {
            this.requests = new ConcurrentHashMap<>();
        }

        // Override Methods
        @Override
        public String toString() {
            int numEndpoints = requests.size();
            int numRequests = requests.entrySet()
                    .stream()
                    .map(Map.Entry::getValue)
                    .mapToInt(arr -> arr.length)
                    .sum();
            return "Bucket [" + numEndpoints + " Endpoint" + (numEndpoints == 1 ? "" : "s") + ", " + numRequests + " Requests]";
        }

        boolean canAccept(RequestURI discordRequestURI) {
            return (MapHelper.countKeyOccurrences(requests, discordRequestURI) < ratelimiterImpl.getLimit(discordRequestURI)
                    .get());
        }

        void addRequest(RequestURI discordRequestURI, Runnable requestExecution) throws LimitExceededException {
            synchronized (bucketQueue) {
                if (!MapHelper.containsKey(requests, discordRequestURI))
                    requests.put(discordRequestURI, new Runnable[0]);
                Runnable[] getOrDefault = MapHelper.getEquals(requests, discordRequestURI, null); // null because value will never be absent
                AtomicInteger limit = ratelimiterImpl.getLimit(discordRequestURI);
                AtomicInteger remaining = ratelimiterImpl.getRemaining(discordRequestURI);
                if (limit.get() < getOrDefault.length) throw new LimitExceededException("Bucket Limit exceeded!");
                Runnable[] arr = addToArray(getOrDefault, requestExecution);
                requests.replace(discordRequestURI, arr);
                remaining.decrementAndGet();
            }
        }

        private Runnable[] addToArray(Runnable[] arr, Runnable add) {
            Runnable[] putArr = new Runnable[arr.length + 1];
            System.arraycopy(arr, 0, putArr, 0, arr.length);
            putArr[putArr.length - 1] = add;
            return putArr;
        }

        void runAll() {
            globalRatelimit.change(requests.size());
            requests.forEach((endpoint, runnables) -> {
                for (Runnable task : runnables) {
                    try {
                        ratelimiterImpl.executePool.execute(task, "Request to " + endpoint);
                    } catch (Exception e) {
                        logger.exception(e, "Exception in Request " + endpoint);
                    }
                }
                requests.remove(endpoint, runnables);
            });
            if (requests.size() > 0) {
                logger.error("Request List of Bucket " + this + " is not empty after execution loop.");
                requests.clear();
            }
        }

        boolean canRun() {
            if (globalRatelimit.get() + requests.size() >= 50)
                return false; // false if global ratelimit counter would be over 50
            int trueC = 0;

            for (RequestURI end : requests.entrySet()
                    .stream()
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList())) {
                final int remaining = ratelimiterImpl.getRemaining(end)
                        .get();
                final int limit = ratelimiterImpl.getLimit(end)
                        .get();
                final Instant reset = ratelimiterImpl.getReset(end)
                        .get();

                if (remaining == 0) {
                    if (reset.isBefore(Instant.now())) {
                        trueC++;
                    }
                } else if ((remaining + requests.entrySet()
                        .stream()
                        .map(Map.Entry::getKey)
                        .map(RequestURI::getAppendix)
                        .mapToInt(a -> 1)
                        .sum()) < limit) {
                    trueC++;
                }
            }

            return trueC == requests.size();
        }

        long waitDuration() {
            long val = 0;
            for (Map.Entry<RequestURI, Runnable[]> endpointEntry : requests.entrySet()) {
                Instant reset = ratelimiterImpl.getReset(endpointEntry.getKey())
                        .get();
                long calc = TimeUnit.SECONDS.toMillis(reset.getEpochSecond()) + TimeUnit.NANOSECONDS.toMillis(reset.getNano());
                if (calc > val) val = calc;
            }
            return val;
        }

        private int numberRequests(DiscordRequestURI discordRequestURI) {
            return requests.entrySet()
                    .stream()
                    .filter(entry -> ((DiscordRequestURI) entry.getKey()).sameRatelimit(discordRequestURI))
                    .mapToInt(entry -> entry.getValue().length)
                    .sum();
        }
    }
}

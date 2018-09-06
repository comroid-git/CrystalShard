package de.kaleidox.crystalshard.internal.core.net.request.ratelimiting;

import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.core.concurrent.ThreadPool;
import de.kaleidox.crystalshard.internal.core.net.request.Endpoint;
import de.kaleidox.logging.Logger;
import de.kaleidox.util.helpers.MapHelper;
import de.kaleidox.util.helpers.QueueHelper;

import javax.naming.LimitExceededException;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

class BucketManager {
    private final static Logger logger = new Logger(BucketManager.class);
    @SuppressWarnings("ALL")
    private final DiscordInternal discord;
    private final Ratelimiting ratelimiting;
    private final ConcurrentLinkedQueue<Bucket> bucketQueue;
    private final ThreadPool atomicPool;

    BucketManager(DiscordInternal discord, Ratelimiting ratelimiting) {
        this.discord = discord;
        this.ratelimiting = ratelimiting;
        this.bucketQueue = new ConcurrentLinkedQueue<>();
        this.atomicPool = new ThreadPool(discord, 1, "BucketManager");

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

    void schedule(Endpoint endpoint, Runnable requestExecution) {
        synchronized (bucketQueue) {
            try {
                if (bucketQueue.isEmpty()) bucketQueue.add(new Bucket());
                boolean success = false;
                while (!success) {
                    Bucket poll = QueueHelper.getTail(bucketQueue);
                    assert poll != null;
                    if (poll.canAccept(endpoint)) {
                        poll.addRequest(endpoint, requestExecution);
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
        private ConcurrentHashMap<Endpoint, Runnable[]> requests;

        Bucket() {
            this.requests = new ConcurrentHashMap<>();
        }

        boolean canAccept(Endpoint endpoint) {
            return (MapHelper.countKeyOccurrences(requests, endpoint) <
                    ratelimiting.getLimit(endpoint).get());
        }

        void addRequest(Endpoint endpoint, Runnable requestExecution) throws LimitExceededException {
            synchronized (bucketQueue) {
                if (!MapHelper.containsKey(requests, endpoint))
                    requests.put(endpoint, new Runnable[0]);
                Runnable[] getOrDefault = MapHelper.getEquals(requests, endpoint, null); // null because value will never be absent
                AtomicInteger limit = ratelimiting.getLimit(endpoint);
                AtomicInteger remaining = ratelimiting.getRemaining(endpoint);
                if (limit.get() < getOrDefault.length)
                    throw new LimitExceededException("Bucket Limit exceeded!");
                Runnable[] arr = addToArray(getOrDefault, requestExecution);
                requests.replace(endpoint, arr);
                remaining.decrementAndGet();
            }
        }

        void runAll() {
            requests.forEach((endpoint, runnables) -> {
                for (Runnable task : runnables) {
                    try {
                        ratelimiting.executePool.execute(task, "Request to " + endpoint);
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
            return true; // todo
        }

        long waitDuration() {
            long val = 0;
            for (Map.Entry<Endpoint, Runnable[]> endpointEntry : requests.entrySet()) {
                Instant reset = ratelimiting.getReset(endpointEntry.getKey()).get();
                long calc = TimeUnit.SECONDS.toMillis(reset.getEpochSecond()) +
                        TimeUnit.NANOSECONDS.toMillis(reset.getNano());
                if (calc > val) val = calc;
            }
            return val;
        }

        @Override
        public String toString() {
            int numEndpoints = requests.size();
            int numRequests = requests.entrySet()
                    .stream()
                    .map(Map.Entry::getValue)
                    .mapToInt(arr -> arr.length)
                    .sum();
            return "Bucket [" + numEndpoints + " Endpoint" +
                    (numEndpoints == 1 ? "" : "s") + ", " + numRequests + " Requests]";
        }

        private int numberRequests(Endpoint endpoint) {
            return requests.entrySet()
                    .stream()
                    .filter(entry -> entry.getKey().equals(endpoint))
                    .mapToInt(entry -> entry.getValue().length)
                    .sum();
        }

        private Runnable[] addToArray(Runnable[] arr, Runnable add) {
            Runnable[] putArr = new Runnable[arr.length + 1];
            System.arraycopy(arr, 0, putArr, 0, arr.length);
            putArr[putArr.length - 1] = add;
            return putArr;
        }
    }
}

package de.kaleidox.crystalshard.internal.core.net.request;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.core.concurrent.ThreadPool;
import de.kaleidox.logging.Logger;
import de.kaleidox.util.Median;
import de.kaleidox.util.helpers.ListHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Ratelimiting {
    private final static Logger logger = new Logger(Ratelimiting.class);
    private final List<Bucket> bucketList;
    private final ThreadPool bucketExecutionPool;

    public Ratelimiting(DiscordInternal discord) {
        this.bucketList = new ArrayList<>();
        this.bucketExecutionPool = new ThreadPool(discord, -1, "Ratelimiter Execution");
        ThreadPool tickPool = new ThreadPool(discord, 1, "Ratelimiter Tick");

        tickPool.execute(() -> {
            //noinspection InfiniteLoopStatement
            while (true) {
                synchronized (bucketList) {
                    try {
                        if (bucketList.size() < 1) bucketList.add(new Bucket());
                        Bucket bucket = bucketList.get(0);
                        bucket.tryRun();
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        logger.exception(e, "RatelimitTickPool sleep interrupted.");
                    }
                }
            }
        });
    }

    public void schedule(Endpoint endpoint, CompletableFuture<JsonNode> future, Runnable requestTask) {
        synchronized (bucketList) {
            if (bucketList.size() == 0)
                bucketList.add(new Bucket());
            while (!bucketList.get(bucketList.size() - 1).addRequest(requestTask, endpoint)) {
                bucketList.add(new Bucket());
            }
        }
    }

    private class Bucket {
        final HashMap<Endpoint, Runnable> requests = new HashMap<>();
        final long birthTime;
        final Median median;
        boolean executed = false;

        Bucket() {
            this.birthTime = System.currentTimeMillis();
            this.median = new Median();
        }

        boolean addRequest(Runnable request, Endpoint endpoint) {
            boolean couldAdd;

            if (requests.containsKey(endpoint) || requests.size() > 49) {
                couldAdd = false;
            } else {
                endpoint.getLocation().getHardcodedRatelimit()
                        .ifPresentOrElse(integer -> median.add(System.currentTimeMillis() + integer),
                                () -> median.add(System.currentTimeMillis() +
                                        Endpoint.RATELIMIT_COOLDOWNS.getOrDefault(endpoint.getLocation(), 200L)));
                requests.put(endpoint, request);
                couldAdd = true;
            }
            tryRun();

            return couldAdd;
        }

        void tryRun() {
            if (median.get() < System.currentTimeMillis()) {
                runAll();
            }
        }

        void runAll() {
            requests.forEach((key, value) -> {
                if (!executed) {
                    ListHelper.moveList(bucketList, -1, Bucket::new);
                    bucketExecutionPool.execute(value);
                    this.executed = true;
                }
            });
        }
    }
}

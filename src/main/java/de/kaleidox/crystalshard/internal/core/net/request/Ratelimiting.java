package de.kaleidox.crystalshard.internal.core.net.request;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.core.concurrent.ThreadPool;
import de.kaleidox.util.helpers.ListHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Ratelimiting {
    private final DiscordInternal discord;
    private final List<Bucket> bucketList;
    private final ThreadPool pool;
    private long retryAtMillis = 0;
    private boolean global;
    private long limit;

    public Ratelimiting(DiscordInternal discord) {
        this.discord = discord;
        this.bucketList = new ArrayList<>();
        this.pool = new ThreadPool(discord, -1, "Ratelimiter");
    }

    public void retryAfter(long retryAfter) {
        this.retryAtMillis = retryAfter + System.currentTimeMillis();
    }

    public long getRetryAtMillis() {
        return retryAtMillis;
    }

    public boolean isGlobal() {
        return global;
    }

    public void setGlobal(boolean global) {
        this.global = global;
    }

    public boolean isReady() {
        return retryAtMillis >= System.currentTimeMillis();
    }

    public void tryRun() {
        if (bucketList.size() < 1) {
            bucketList.add(new Bucket());
        }
        discord.getThreadPool()
                .execute(() -> {
                    Bucket bucket = bucketList.get(0);
                    bucket.requests.forEach(WebRequest::execute);
                    bucketList.remove(bucket);
                    ListHelper.moveList(bucketList, -1, Bucket::new);
                });
    }

    @Deprecated
    public CompletableFuture<JsonNode> scheduleRequest(WebRequest<JsonNode> request) {
        boolean found = false;

        for (int i = 0; !found; i++) {
            if (request.getFirstArgument().isEmpty()) {
                bucketList.get(i).addRequest(request);
                found = true;
            }
            if (!bucketList.get(i).requests
                    .stream()
                    .anyMatch(thisRequest -> thisRequest.getFirstArgument()
                            .orElse("")
                            .equals(request.getFirstArgument().get()))) {
                bucketList.get(i).addRequest(request);
            }
        }

        return request.getFuture();
    }

    public void setLimit(long limit) {
        this.limit = limit;
    }

    public void schedule(Runnable requestTask) {
        pool.execute(requestTask);
    }

    public static class RatelimitBlock extends Throwable {
        Boolean global = null;
        Long retryAfter = null;
        Long limit = null;
        Long remaining = null;

        public void setGlobal(Boolean global) {
            this.global = global;
        }

        public void setRetryAfter(Long retryAfter) {
            this.retryAfter = retryAfter;
        }

        public void setLimit(Long limit) {
            this.limit = limit;
        }

        public void setRemaining(Long remaining) {
            this.remaining = remaining;
        }
    }

    class Bucket {
        final List<WebRequest<JsonNode>> requests = new ArrayList<>();
        final long birthTime;

        Bucket() {
            this.birthTime = System.currentTimeMillis();
        }

        public CompletableFuture<JsonNode> addRequest(WebRequest<JsonNode> request) {
            this.requests.add(request);
            return request.getFuture();
        }
    }
}

package de.kaleidox.crystalshard.internal.core;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.util.ListHelper;
import de.kaleidox.websocket.WebRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Ratelimiting {
    private final DiscordInternal discord;
    private final List<Bucket> bucketList;
    private long retryAtMillis = 0;
    private boolean global;
    private long limit;

    public Ratelimiting(DiscordInternal discord) {
        this.discord = discord;
        this.bucketList = new ArrayList<>();
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

    public CompletableFuture<JsonNode> schedule(WebRequest<JsonNode> request) {
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

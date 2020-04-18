package org.comroid.crystalshard.core;

import org.comroid.crystalshard.CrystalShard;
import org.comroid.crystalshard.api.DiscordBot;
import org.comroid.crystalshard.api.entity.Snowflake;
import org.comroid.dreadpool.ThreadPool;
import org.comroid.restless.REST;
import org.comroid.uniform.cache.Cache;
import org.comroid.uniform.cache.ProvidedCache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DiscordBotImpl implements DiscordBot {
    private final String                 token;
    private final ThreadPool             threadPool;
    private final Cache<Long, Snowflake> entityCache;
    private final REST<DiscordBot>       restClient;
    private final List<Shard>            shards;

    public DiscordBotImpl(String token) {
        this.token = token;

        this.threadPool  = ThreadPool.fixedSize(CrystalShard.THREAD_GROUP, 8);
        this.entityCache = new ProvidedCache<>(threadPool, any -> null);
        this.restClient  = new REST<>(CrystalShard.HTTP_ADAPTER, this, CrystalShard.SERIALIZATION_ADAPTER);

        this.shards = initShards();
    }

    private List<Shard> initShards() {
        final List<ShardImpl> shards = new ArrayList<>();

        return Collections.unmodifiableList(shards);
    }

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public ThreadPool getThreadPool() {
        return threadPool;
    }

    @Override
    public Cache<Long, Snowflake> getEntityCache() {
        return entityCache;
    }

    @Override
    public REST<DiscordBot> getRestClient() {
        return restClient;
    }

    @Override
    public List<Shard> getShards() {
        return shards;
    }

    private static class ShardImpl implements Shard {
        private final int shardId;

        private ShardImpl(int shardId) {
            this.shardId = shardId;
        }

        @Override
        public int getShardID() {
            return shardId;
        }
    }
}

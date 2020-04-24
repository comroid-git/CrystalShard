package org.comroid.crystalshard;

import java.util.List;
import java.util.stream.IntStream;

import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.dreadpool.ThreadPool;
import org.comroid.restless.REST;
import org.comroid.uniform.cache.BasicCache;
import org.comroid.uniform.cache.Cache;

public interface DiscordBot {
    String getToken();

    ThreadPool getThreadPool();

    Cache<Long, Snowflake> getEntityCache();

    REST<DiscordBot> getRestClient();

    default int getShardCount() {
        return getShards().size();
    }

    List<DiscordBot.Shard> getShards();

    final class Support {
        private static final class Impl implements DiscordBot {
            private Impl(String token, int shardCount, ThreadGroup group) {
                this.token       = token;
                this.threadPool  = ThreadPool.fixedSize(group, 8 * shardCount);
                this.entityCache = new BasicCache<>(500);
                this.restClient  = new REST<>(CrystalShard.HTTP_ADAPTER, this, CrystalShard.SERIALIZATION_ADAPTER);
                this.shards      = IntStream.range(0, shardCount)
                        .mapToObj(it -> new ShardImpl(this, it));
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
            private final String                 token;
            private final ThreadPool             threadPool;
            private final Cache<Long, Snowflake> entityCache;
            private final REST<DiscordBot>       restClient;
            private final List<Shard>            shards;
        }

        private static final class ShardImpl implements Shard {
            public ShardImpl(DiscordBot bot, int myId) {
                this.bot = bot;
                this.id  = myId;
            }

            @Override
            public int getShardID() {
                return id;
            }
            private final DiscordBot bot;
            private final int        id;
        }
    }

    interface Shard {
        int getShardID();
    }
}

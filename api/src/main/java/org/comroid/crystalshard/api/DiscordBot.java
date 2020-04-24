package org.comroid.crystalshard.api;

import java.util.List;

import org.comroid.crystalshard.api.entity.Snowflake;
import org.comroid.dreadpool.ThreadPool;
import org.comroid.restless.REST;
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

    interface Shard {
        int getShardID();
    }
}

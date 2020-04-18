package org.comroid.crystalshard.api;

import org.comroid.crystalshard.api.entity.Snowflake;
import org.comroid.dreadpool.ThreadPool;
import org.comroid.restless.REST;
import org.comroid.uniform.cache.Cache;

import java.util.List;

public interface DiscordBot {
    String getToken();

    ThreadPool getThreadPool();

    Cache<Long, Snowflake> getEntityCache();

    REST<DiscordBot> getRestClient();

    List<DiscordBot.Shard> getShards();

    default int getShardCount() {
        return getShards().size();
    }

    interface Shard {
        int getShardID();
    }
}

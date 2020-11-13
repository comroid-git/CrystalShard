package org.comroid.crystalshard.core.cache;

import org.comroid.crystalshard.entity.DiscordEntity;
import org.comroid.crystalshard.model.BotBound;
import org.comroid.uniform.cache.AbstractCache;

public final class SnowflakeCache extends AbstractCache<SnowflakeSelector, DiscordEntity> implements BotBound {
    public SnowflakeCache() {
        super(cache);
    }
}

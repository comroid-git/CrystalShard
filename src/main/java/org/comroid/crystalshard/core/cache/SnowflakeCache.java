package org.comroid.crystalshard.core.cache;

import org.comroid.common.map.TrieStringMap;
import org.comroid.uniform.cache.BasicCache;
import org.jetbrains.annotations.NotNull;

public final class SnowflakeCache extends BasicCache<Long, SnowflakeSelector> {
    public SnowflakeCache() {
        super(250, () -> new TrieStringMap<>(String::valueOf, Long::parseLong));
    }

    @Override
    public @NotNull Reference<Long, SnowflakeSelector> getReference(Long key, boolean createIfAbsent) {
        final Reference<Long, SnowflakeSelector> ref = super.getReference(key, createIfAbsent);

        if (ref.isNull())
            ref.set(new SnowflakeSelector(key));

        return ref;
    }
}

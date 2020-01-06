package org.comroid.crystalshard.abstraction.cache;

import org.comroid.crystalshard.abstraction.entity.AbstractSnowflake;
import org.comroid.crystalshard.api.Discord;
import org.comroid.crystalshard.core.cache.Cacheable;

import com.alibaba.fastjson.JSONObject;

import static org.comroid.crystalshard.util.Util.hackCast;

public abstract class AbstractCacheable<Self extends AbstractSnowflake<Self> & Cacheable>
        extends AbstractSnowflake<Self>
        implements Cacheable {
    protected AbstractCacheable(Discord api, JSONObject data) {
        super(api, data);

        initCacheable();
    }

    protected void initCacheable() {
        final CacheInformation<?> cacheInformation = Cacheable.getCacheInfo(this)
                .orElse(null);

        if (cacheInformation == null) {
            api.getCacheManager().set(hackCast(this.getClass()), getID(), hackCast(this));

            return;
        }

        Class<?> parentType = cacheInformation.getParentClass();
        long parentId = cacheInformation.getIDfromParent();

        if (cacheInformation.type() == 1)
            api.getCacheManager().setSingleton(hackCast(parentType), hackCast(this.getClass()), parentId, hackCast(this));
        else if (cacheInformation.type() == 0) api.getCacheManager().setMember(
                hackCast(parentType),
                hackCast(this.getClass()),
                parentId,
                getID(),
                hackCast(this)
        );
        else throw new IllegalStateException("Unknown Cacheable Type: " + cacheInformation.type());
    }
}

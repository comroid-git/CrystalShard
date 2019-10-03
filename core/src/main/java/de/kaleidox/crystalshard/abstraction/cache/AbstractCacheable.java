package de.kaleidox.crystalshard.abstraction.cache;

import java.util.Optional;
import java.util.OptionalLong;

import de.kaleidox.crystalshard.abstraction.entity.AbstractSnowflake;
import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.core.api.cache.Cacheable;

import com.fasterxml.jackson.databind.JsonNode;

import static de.kaleidox.crystalshard.util.Util.hackCast;

public abstract class AbstractCacheable<Self extends AbstractSnowflake<Self> & Cacheable>
        extends AbstractSnowflake<Self>
        implements Cacheable {
    protected AbstractCacheable(Discord api, JsonNode data) {
        super(api, data);

        initCacheable();
    }

    @Override
    public void update(JsonNode data) {
        updateFromJson(data);
    }

    protected void initCacheable() {
        if (isSubcacheMember()) {
            Optional<Class<? extends Cacheable>> parentTypeOpt = getCacheParentType();
            OptionalLong parentIdOpt = getCacheParentID();

            if (parentTypeOpt.isEmpty() || parentIdOpt.isEmpty())
                throw new IllegalStateException("Cacheable is flagged as a subcache member but " +
                        "does not return parent information!");

            Class<? extends Cacheable> parentType = parentTypeOpt.get();
            long parentId = parentIdOpt.getAsLong();

            if (isSingletonType())
                api.getCacheManager().setSingleton(hackCast(parentType), hackCast(this.getClass()), parentId, hackCast(this));
            else api.getCacheManager().setMember(
                    hackCast(parentType),
                    hackCast(this.getClass()),
                    parentId,
                    getID(),
                    hackCast(this)
            );
        } else api.getCacheManager().set(hackCast(this.getClass()), getID(), hackCast(this));
    }
}

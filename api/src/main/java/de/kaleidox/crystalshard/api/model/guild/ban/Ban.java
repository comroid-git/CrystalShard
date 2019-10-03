package de.kaleidox.crystalshard.api.model.guild.ban;

import java.util.Optional;
import java.util.OptionalLong;

import de.kaleidox.crystalshard.api.entity.user.User;
import de.kaleidox.crystalshard.core.api.cache.Cacheable;

public interface Ban extends Cacheable {
    Optional<String> getReason();

    User getBannedUser();

    @Override
    default OptionalLong getCacheParentID() {
        return OptionalLong.of(getBannedUser().getID());
    }

    @Override
    default Optional<Class<? extends Cacheable>> getCacheParentType() {
        return Optional.of(User.class);
    }

    @Override
    default boolean isSubcacheMember() {
        return true;
    }
}

package de.comroid.crystalshard.api.model.guild.ban;

import java.util.Optional;

import de.comroid.crystalshard.api.entity.user.User;
import de.comroid.crystalshard.core.api.cache.Cacheable;
import de.comroid.crystalshard.util.model.serialization.JsonDeserializable;
import de.comroid.crystalshard.util.model.serialization.JsonTrait;
import de.comroid.crystalshard.util.model.serialization.JsonTraits;

import com.fasterxml.jackson.databind.JsonNode;

import static de.comroid.crystalshard.util.model.serialization.JsonTrait.identity;
import static de.comroid.crystalshard.util.model.serialization.JsonTrait.underlying;

@JsonTraits(Ban.Trait.class)
public interface Ban extends Cacheable, JsonDeserializable {
    default Optional<String> getReason() {
        return wrapTraitValue(Trait.REASON);
    }

    default User getBannedUser() {
        return getTraitValue(Trait.USER);
    }

    interface Trait {
        JsonTrait<String, String> REASON = identity(JsonNode::asText, "reason");
        JsonTrait<JsonNode, User> USER = underlying("user", User.class);
    }

    @Override
    default Optional<Long> getCacheParentID() {
        return Optional.of(getBannedUser().getID());
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

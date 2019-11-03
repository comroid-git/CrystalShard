package de.comroid.crystalshard.api.model.guild.ban;

import java.util.Optional;

import de.comroid.crystalshard.api.entity.user.User;
import de.comroid.crystalshard.core.api.cache.Cacheable;
import de.comroid.crystalshard.util.model.serialization.JsonDeserializable;
import de.comroid.crystalshard.util.model.serialization.JsonBinding;
import de.comroid.crystalshard.util.model.serialization.JsonTraits;

import com.fasterxml.jackson.databind.JsonNode;

import static de.comroid.crystalshard.core.api.cache.Cacheable.makeSubcacheableInfo;
import static de.comroid.crystalshard.util.model.serialization.JsonBinding.identity;
import static de.comroid.crystalshard.util.model.serialization.JsonBinding.underlying;

@JsonTraits(Ban.Trait.class)
public interface Ban extends Cacheable, JsonDeserializable {
    @CacheInformation.Marker
    CacheInformation<User> CACHE_INFORMATION = makeSubcacheableInfo(User.class, Ban::getBannedUser);
    
    default Optional<String> getReason() {
        return wrapTraitValue(Trait.REASON);
    }

    default User getBannedUser() {
        return getTraitValue(Trait.USER);
    }

    interface Trait {
        JsonBinding<String, String> REASON = identity(JsonNode::asText, "reason");
        JsonBinding<JsonNode, User> USER = underlying("user", User.class);
    }
}

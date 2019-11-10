package de.comroid.crystalshard.api.model.guild.ban;

import java.util.Optional;

import de.comroid.crystalshard.adapter.MainAPI;
import de.comroid.crystalshard.api.entity.user.User;
import de.comroid.crystalshard.core.cache.Cacheable;
import de.comroid.crystalshard.util.model.serialization.JSONBinding;
import de.comroid.crystalshard.util.model.serialization.JsonDeserializable;
import de.comroid.crystalshard.util.model.serialization.JSONBindingLocation;

import com.alibaba.fastjson.JSONObject;

import static de.comroid.crystalshard.core.cache.Cacheable.makeSubcacheableInfo;
import static de.comroid.crystalshard.util.model.serialization.JSONBinding.identity;
import static de.comroid.crystalshard.util.model.serialization.JSONBinding.require;

@MainAPI
@JSONBindingLocation(Ban.Trait.class)
public interface Ban extends Cacheable, JsonDeserializable {
    @CacheInformation.Marker
    CacheInformation<User> CACHE_INFORMATION = makeSubcacheableInfo(User.class, Ban::getBannedUser);

    default Optional<String> getReason() {
        return wrapBindingValue(JSON.REASON);
    }

    default User getBannedUser() {
        return getBindingValue(JSON.USER);
    }

    interface JSON {
        JSONBinding.OneStage<String> REASON = identity("reason", JSONObject::getString);
        JSONBinding.TwoStage<JSONObject, User> USER = require("user", User.class);
    }
}

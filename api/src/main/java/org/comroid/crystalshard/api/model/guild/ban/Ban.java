package org.comroid.crystalshard.api.model.guild.ban;

import java.util.Optional;

import org.comroid.crystalshard.adapter.MainAPI;
import org.comroid.crystalshard.api.entity.user.User;
import org.comroid.crystalshard.core.cache.Cacheable;
import org.comroid.crystalshard.util.model.serialization.JSONBinding;
import org.comroid.crystalshard.util.model.serialization.JsonDeserializable;
import org.comroid.crystalshard.util.model.serialization.JSONBindingLocation;

import com.alibaba.fastjson.JSONObject;

import static org.comroid.crystalshard.core.cache.Cacheable.makeSubcacheableInfo;
import static org.comroid.crystalshard.util.model.serialization.JSONBinding.identity;
import static org.comroid.crystalshard.util.model.serialization.JSONBinding.require;

@MainAPI
@JSONBindingLocation(Ban.JSON.class)
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

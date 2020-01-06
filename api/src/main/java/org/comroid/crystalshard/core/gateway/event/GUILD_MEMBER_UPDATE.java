package org.comroid.crystalshard.core.gateway.event;

// https://discordapp.com/developers/docs/topics/gateway#guild-member-update

import java.util.Collection;
import java.util.Optional;

import org.comroid.crystalshard.adapter.MainAPI;
import org.comroid.crystalshard.api.entity.guild.Guild;
import org.comroid.crystalshard.api.entity.guild.Role;
import org.comroid.crystalshard.api.entity.user.User;
import org.comroid.crystalshard.core.cache.CacheManager;
import org.comroid.crystalshard.util.model.serialization.JSONBinding;
import org.comroid.crystalshard.util.model.serialization.JSONBindingLocation;

import com.alibaba.fastjson.JSONObject;

import static org.comroid.crystalshard.util.model.serialization.JSONBinding.cache;
import static org.comroid.crystalshard.util.model.serialization.JSONBinding.identity;
import static org.comroid.crystalshard.util.model.serialization.JSONBinding.serializableCollection;
import static org.comroid.crystalshard.util.model.serialization.JSONBinding.require;

@MainAPI
@JSONBindingLocation(GUILD_MEMBER_UPDATE.JSON.class)
public interface GUILD_MEMBER_UPDATE extends GatewayEventBase {
    default Guild getGuild() {
        return getBindingValue(JSON.GUILD);
    }

    default Collection<Role> getRole() {
        return getBindingValue(JSON.ROLE);
    }

    default User getUser() {
        return getBindingValue(JSON.MEMBER);
    }

    default Optional<String> getNickname() {
        return wrapBindingValue(JSON.NICKNAME);
    }
    
    interface JSON {
        JSONBinding.TwoStage<Long, Guild> GUILD = cache("guild_id", CacheManager::getGuildByID);
        JSONBinding.TriStage<JSONObject, Role> ROLE = serializableCollection("roles", Role.class);
        JSONBinding.TwoStage<JSONObject, User> MEMBER = require("user", User.class);
        JSONBinding.OneStage<String> NICKNAME = identity("nick", JSONObject::getString);
    }
}

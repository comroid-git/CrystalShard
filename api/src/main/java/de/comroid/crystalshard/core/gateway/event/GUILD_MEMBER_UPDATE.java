package de.comroid.crystalshard.core.gateway.event;

// https://discordapp.com/developers/docs/topics/gateway#guild-member-update

import java.util.Collection;
import java.util.Optional;

import de.comroid.crystalshard.adapter.MainAPI;
import de.comroid.crystalshard.api.entity.guild.Guild;
import de.comroid.crystalshard.api.entity.guild.Role;
import de.comroid.crystalshard.api.entity.user.User;
import de.comroid.crystalshard.core.cache.CacheManager;
import de.comroid.crystalshard.util.model.serialization.JSONBinding;
import de.comroid.crystalshard.util.model.serialization.JSONBindingLocation;

import com.alibaba.fastjson.JSONObject;

import static de.comroid.crystalshard.util.model.serialization.JSONBinding.cache;
import static de.comroid.crystalshard.util.model.serialization.JSONBinding.identity;
import static de.comroid.crystalshard.util.model.serialization.JSONBinding.serializableCollection;
import static de.comroid.crystalshard.util.model.serialization.JSONBinding.require;

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

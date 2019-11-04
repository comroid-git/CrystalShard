package de.comroid.crystalshard.core.api.gateway.event;

// https://discordapp.com/developers/docs/topics/gateway#guild-member-remove

import de.comroid.crystalshard.adapter.MainAPI;
import de.comroid.crystalshard.api.entity.guild.Guild;
import de.comroid.crystalshard.api.entity.user.User;
import de.comroid.crystalshard.core.api.cache.CacheManager;
import de.comroid.crystalshard.util.model.serialization.JSONBindingLocation;
import de.comroid.crystalshard.util.model.serialization.JSONBinding;

import com.alibaba.fastjson.JSONObject;

import static de.comroid.crystalshard.util.model.serialization.JSONBinding.cache;
import static de.comroid.crystalshard.util.model.serialization.JSONBinding.require;

@MainAPI
@JSONBindingLocation(GUILD_MEMBER_REMOVE.JSON.class)
public interface GUILD_MEMBER_REMOVE extends GatewayEventBase {
    default Guild getGuild() {
        return getBindingValue(JSON.GUILD);
    }

    default User getUser() {
        return getBindingValue(JSON.USER);
    }
    
    interface JSON {
        JSONBinding.TwoStage<Long, Guild> GUILD = cache("guild_id", CacheManager::getGuildByID);
        JSONBinding.TwoStage<JSONObject, User> USER = require("user", User.class);
    }
}

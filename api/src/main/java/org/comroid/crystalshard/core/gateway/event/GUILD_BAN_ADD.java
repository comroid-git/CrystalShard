package org.comroid.crystalshard.core.gateway.event;

// https://discordapp.com/developers/docs/topics/gateway#guild-ban-add

import org.comroid.crystalshard.adapter.MainAPI;
import org.comroid.crystalshard.api.entity.guild.Guild;
import org.comroid.crystalshard.api.entity.user.User;
import org.comroid.crystalshard.core.cache.CacheManager;
import org.comroid.crystalshard.util.model.serialization.JSONBindingLocation;
import org.comroid.crystalshard.util.model.serialization.JSONBinding;

import com.alibaba.fastjson.JSONObject;

import static org.comroid.crystalshard.util.model.serialization.JSONBinding.cache;
import static org.comroid.crystalshard.util.model.serialization.JSONBinding.require;

@MainAPI
@JSONBindingLocation(GUILD_BAN_ADD.JSON.class)
public interface GUILD_BAN_ADD extends GatewayEventBase {
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

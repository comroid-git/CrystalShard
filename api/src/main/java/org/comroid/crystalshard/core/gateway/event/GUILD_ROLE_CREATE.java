package org.comroid.crystalshard.core.gateway.event;

// https://discordapp.com/developers/docs/topics/gateway#guild-role-create

import org.comroid.crystalshard.adapter.MainAPI;
import org.comroid.crystalshard.api.entity.guild.Guild;
import org.comroid.crystalshard.api.entity.guild.Role;
import org.comroid.crystalshard.core.cache.CacheManager;
import org.comroid.crystalshard.util.model.serialization.JSONBinding;
import org.comroid.crystalshard.util.model.serialization.JSONBindingLocation;

import com.alibaba.fastjson.JSONObject;

import static org.comroid.crystalshard.util.model.serialization.JSONBinding.cache;
import static org.comroid.crystalshard.util.model.serialization.JSONBinding.require;

@MainAPI
@JSONBindingLocation(GUILD_ROLE_CREATE.JSON.class)
public interface GUILD_ROLE_CREATE extends GatewayEventBase {
    default Guild getGuild() {
        return getBindingValue(JSON.GUILD);
    }

    default Role getRole() {
        return getBindingValue(JSON.ROLE);
    }
    
    interface JSON {
        JSONBinding.TwoStage<Long, Guild> GUILD = cache("guild_id", CacheManager::getGuildByID);
        JSONBinding.TwoStage<JSONObject, Role> ROLE = require("role", Role.class);
    }
}

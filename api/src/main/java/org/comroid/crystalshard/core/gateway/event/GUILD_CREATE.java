package org.comroid.crystalshard.core.gateway.event;

// https://discordapp.com/developers/docs/topics/gateway#guild-create

import org.comroid.crystalshard.adapter.MainAPI;
import org.comroid.crystalshard.api.entity.guild.Guild;
import org.comroid.crystalshard.util.model.serialization.JSONBindingLocation;
import org.comroid.crystalshard.util.model.serialization.JSONBinding;

import com.alibaba.fastjson.JSONObject;

@MainAPI
@JSONBindingLocation(GUILD_CREATE.JSON.class)
public interface GUILD_CREATE extends GatewayEventBase {
    default Guild getGuild() {
        return getBindingValue(JSON.GUILD);
    }
    
    interface JSON {
        JSONBinding.TwoStage<JSONObject, Guild> GUILD = JSONBinding.rooted(Guild.class);
    }
}

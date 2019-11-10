package de.comroid.crystalshard.core.gateway.event;

// https://discordapp.com/developers/docs/topics/gateway#guild-update

import de.comroid.crystalshard.adapter.MainAPI;
import de.comroid.crystalshard.api.entity.guild.Guild;
import de.comroid.crystalshard.util.model.serialization.JSONBindingLocation;
import de.comroid.crystalshard.util.model.serialization.JSONBinding;

import com.alibaba.fastjson.JSONObject;

@MainAPI
@JSONBindingLocation(GUILD_UPDATE.JSON.class)
public interface GUILD_UPDATE extends GatewayEventBase {
    default Guild getGuild() {
        return getBindingValue(JSON.GUILD);
    }
    
    interface JSON {
        JSONBinding.TwoStage<JSONObject, Guild> GUILD = JSONBinding.rooted(Guild.class);
    }
}

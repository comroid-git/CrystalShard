package org.comroid.crystalshard.core.gateway.event;

import java.net.URL;

import javax.print.DocFlavor;

import org.comroid.crystalshard.adapter.MainAPI;
import org.comroid.crystalshard.api.entity.guild.Guild;
import org.comroid.crystalshard.core.cache.CacheManager;
import org.comroid.crystalshard.util.Util;
import org.comroid.crystalshard.util.model.serialization.JSONBinding;
import org.comroid.crystalshard.util.model.serialization.JSONBindingLocation;

import com.alibaba.fastjson.JSONObject;

@MainAPI
@JSONBindingLocation(VOICE_SERVER_UPDATE.JSON.class)
public interface VOICE_SERVER_UPDATE extends GatewayEventBase {
    default String getToken() {
        return getBindingValue(JSON.TOKEN);
    }

    default Guild getGuild() {
        return getBindingValue(JSON.GUILD);
    }

    default URL getEndpointURL() {
        return getBindingValue(JSON.ENDPOINT);
    }
    
    interface JSON {
        JSONBinding.OneStage<String> TOKEN = JSONBinding.identity("token", JSONObject::getString);
        JSONBinding.TwoStage<Long, Guild> GUILD = JSONBinding.cache("guild_id", CacheManager::getGuildByID);
        JSONBinding.TwoStage<String, URL> ENDPOINT = JSONBinding.simple("endpoint", JSONObject::getString, Util::createUrl$rethrow);
    }
}

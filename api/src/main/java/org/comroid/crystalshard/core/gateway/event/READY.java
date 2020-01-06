package org.comroid.crystalshard.core.gateway.event;

// https://discordapp.com/developers/docs/topics/gateway#ready

import java.util.Collection;
import java.util.Optional;

import org.comroid.crystalshard.adapter.Adapter;
import org.comroid.crystalshard.adapter.MainAPI;
import org.comroid.crystalshard.api.entity.channel.PrivateChannel;
import org.comroid.crystalshard.api.entity.guild.Guild;
import org.comroid.crystalshard.api.model.user.Yourself;
import org.comroid.crystalshard.util.model.serialization.JSONBinding;
import org.comroid.crystalshard.util.model.serialization.JSONBindingLocation;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import static org.comroid.crystalshard.util.model.serialization.JSONBinding.api;
import static org.comroid.crystalshard.util.model.serialization.JSONBinding.identity;
import static org.comroid.crystalshard.util.model.serialization.JSONBinding.serializableCollection;
import static org.comroid.crystalshard.util.model.serialization.JSONBinding.simple;

@MainAPI
@JSONBindingLocation(READY.JSON.class)
public interface READY extends GatewayEventBase {
    default int getGatewayProtocolVersion() {
        return getBindingValue(JSON.GATEWAY_PROTOCOL_VERSION);
    }

    default Yourself getYourself() {
        return getBindingValue(JSON.YOURSELF);
    }

    default Collection<PrivateChannel> getPrivateChannels() {
        return getBindingValue(JSON.PRIVATE_CHANNELS);
    }

    default Collection<Guild> getGuilds() {
        return getBindingValue(JSON.GUILDS);
    }

    default String getSessionID() {
        return getBindingValue(JSON.SESSION_ID);
    }

    default Optional<int[]> getShardInformation() {
        return wrapBindingValue(JSON.SHARD_INFORMATION);
    }

    interface JSON {
        JSONBinding.OneStage<Integer> GATEWAY_PROTOCOL_VERSION = identity("v", JSONObject::getInteger);
        JSONBinding.TwoStage<JSONObject, Yourself> YOURSELF = api("user", JSONObject::getJSONObject, (api, json) -> Adapter.require(Yourself.class, api, json));
        JSONBinding.TriStage<JSONObject, PrivateChannel> PRIVATE_CHANNELS = serializableCollection("private_channels", PrivateChannel.class);
        JSONBinding.TriStage<JSONObject, Guild> GUILDS = serializableCollection("guilds", Guild.class);
        JSONBinding.OneStage<String> SESSION_ID = identity("session_id", JSONObject::getString);
        JSONBinding.TwoStage<JSONArray, int[]> SHARD_INFORMATION = simple("shard", JSONObject::getJSONArray, array -> new int[]{array.getInteger(0), array.getInteger(1)});
    }
}

package de.comroid.crystalshard.core.api.gateway.event;

// https://discordapp.com/developers/docs/topics/gateway#channel-delete

import de.comroid.crystalshard.adapter.MainAPI;
import de.comroid.crystalshard.api.entity.channel.Channel;
import de.comroid.crystalshard.util.model.serialization.JSONBindingLocation;
import de.comroid.crystalshard.util.model.serialization.JSONBinding;

import com.alibaba.fastjson.JSONObject;

@MainAPI
@JSONBindingLocation(CHANNEL_DELETE.JSON.class)
public interface CHANNEL_DELETE extends GatewayEventBase {
    default Channel getChannel() {
        return getBindingValue(JSON.CHANNEL);
    }
    
    interface JSON {
        JSONBinding.TwoStage<JSONObject, Channel> CHANNEL = JSONBinding.rooted(Channel.class);
    }
}

package de.comroid.crystalshard.core.api.gateway.event;

// https://discordapp.com/developers/docs/topics/gateway#channel-create

import de.comroid.crystalshard.adapter.MainAPI;
import de.comroid.crystalshard.api.entity.channel.Channel;
import de.comroid.crystalshard.util.model.serialization.JSONBinding;
import de.comroid.crystalshard.util.model.serialization.JSONBindingLocation;

import com.alibaba.fastjson.JSONObject;

@MainAPI
@JSONBindingLocation(CHANNEL_CREATE.Trait.class)
public interface CHANNEL_CREATE extends GatewayEventBase {
    default Channel getChannel() {
        return getBindingValue(JSON.CHANNEL);
    }

    interface JSON {
        JSONBinding.TwoStage<JSONObject, Channel> CHANNEL = JSONBinding.rooted(Channel.class);
    }
}

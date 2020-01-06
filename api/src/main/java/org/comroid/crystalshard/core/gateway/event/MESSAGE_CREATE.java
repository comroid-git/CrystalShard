package org.comroid.crystalshard.core.gateway.event;

// https://discordapp.com/developers/docs/topics/gateway#message-create

import org.comroid.crystalshard.adapter.Constructor;
import org.comroid.crystalshard.adapter.MainAPI;
import org.comroid.crystalshard.api.entity.message.Message;
import org.comroid.crystalshard.util.model.serialization.JSONBinding;
import org.comroid.crystalshard.util.model.serialization.JSONBindingLocation;

import com.alibaba.fastjson.JSONObject;

import static org.comroid.crystalshard.util.model.serialization.JSONBinding.rooted;

@MainAPI
@JSONBindingLocation(MESSAGE_CREATE.JSON.class)
public interface MESSAGE_CREATE extends GatewayEventBase {
    default Message getMessage() {
        return getBindingValue(JSON.MESSAGE);
    }
    
    interface JSON {
        JSONBinding.TwoStage<JSONObject, Message> MESSAGE = rooted(Message.class);
    }
}

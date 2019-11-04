package de.comroid.crystalshard.core.api.gateway.event;

// https://discordapp.com/developers/docs/topics/gateway#message-create

import de.comroid.crystalshard.adapter.MainAPI;
import de.comroid.crystalshard.api.entity.message.Message;
import de.comroid.crystalshard.util.model.serialization.JSONBinding;
import de.comroid.crystalshard.util.model.serialization.JSONBindingLocation;

import com.alibaba.fastjson.JSONObject;

import static de.comroid.crystalshard.util.model.serialization.JSONBinding.rooted;

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

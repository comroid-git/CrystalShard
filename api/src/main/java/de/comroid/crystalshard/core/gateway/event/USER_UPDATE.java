package de.comroid.crystalshard.core.gateway.event;

// https://discordapp.com/developers/docs/topics/gateway#user-update

import de.comroid.crystalshard.adapter.MainAPI;
import de.comroid.crystalshard.api.entity.user.User;
import de.comroid.crystalshard.util.model.serialization.JSONBinding;
import de.comroid.crystalshard.util.model.serialization.JSONBindingLocation;

import com.alibaba.fastjson.JSONObject;

@MainAPI
@JSONBindingLocation(USER_UPDATE.JSON.class)
public interface USER_UPDATE extends GatewayEventBase {
    default User getUser() {
        return getBindingValue(JSON.USER);
    }
    
    interface JSON {
        JSONBinding.TwoStage<JSONObject, User> USER = JSONBinding.rooted(User.class);
    }
}

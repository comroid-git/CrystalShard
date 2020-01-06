package org.comroid.crystalshard.core.gateway.event;

// https://discordapp.com/developers/docs/topics/gateway#invalid-session

import org.comroid.crystalshard.adapter.MainAPI;
import org.comroid.crystalshard.util.model.serialization.JSONBinding;
import org.comroid.crystalshard.util.model.serialization.JSONBindingLocation;

import com.alibaba.fastjson.JSONObject;

@MainAPI
@JSONBindingLocation(INVALID_SESSION.JSON.class)
public interface INVALID_SESSION extends GatewayEventBase {
    default boolean isResumable() {
        return getBindingValue(JSON.RESUMABLE);
    }
    
    interface JSON {
        JSONBinding.TwoStage<JSONObject, Boolean> RESUMABLE = JSONBinding.simple(null, (json, key) -> json, json -> json.getBoolean("d"));
    }
}

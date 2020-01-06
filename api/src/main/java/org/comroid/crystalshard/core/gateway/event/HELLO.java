package org.comroid.crystalshard.core.gateway.event;

import org.comroid.crystalshard.adapter.MainAPI;
import org.comroid.crystalshard.util.model.serialization.JSONBinding;
import org.comroid.crystalshard.util.model.serialization.JSONBindingLocation;

import com.alibaba.fastjson.JSONObject;

@MainAPI
@JSONBindingLocation(HELLO.JSON.class)
public interface HELLO extends GatewayEventBase {
    default int getHeartbeatInterval() {
        return getBindingValue(JSON.HEARTBEAT_INTERVAL);
    }

    interface JSON {
        JSONBinding.OneStage<Integer> HEARTBEAT_INTERVAL = JSONBinding.identity("heartbeat_interval", JSONObject::getInteger);
    }
}

package de.comroid.crystalshard.core.gateway.event;

import de.comroid.crystalshard.adapter.MainAPI;
import de.comroid.crystalshard.util.model.serialization.JSONBinding;
import de.comroid.crystalshard.util.model.serialization.JSONBindingLocation;

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

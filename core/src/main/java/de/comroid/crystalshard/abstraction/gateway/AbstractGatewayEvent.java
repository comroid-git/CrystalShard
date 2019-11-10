package de.comroid.crystalshard.abstraction.gateway;

import de.comroid.crystalshard.abstraction.event.AbstractEvent;
import de.comroid.crystalshard.api.Discord;
import de.comroid.crystalshard.core.gateway.Gateway;
import de.comroid.crystalshard.core.gateway.event.GatewayEventBase;

import com.alibaba.fastjson.JSONObject;

public abstract class AbstractGatewayEvent extends AbstractEvent implements GatewayEventBase {
    protected final Gateway gateway;

    protected AbstractGatewayEvent(Discord api, JSONObject data) {
        super(api, data);

        gateway = api.getGateway();
        
        affects(gateway);
    }

    @Override
    public Gateway getGateway() {
        return gateway;
    }
}

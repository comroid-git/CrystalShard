package org.comroid.crystalshard.abstraction.gateway;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.comroid.crystalshard.abstraction.serialization.AbstractJsonDeserializable;
import org.comroid.crystalshard.api.Discord;
import org.comroid.crystalshard.api.event.EventBase;
import org.comroid.crystalshard.api.event.EventHandler;
import org.comroid.crystalshard.core.gateway.Gateway;
import org.comroid.crystalshard.core.gateway.event.GatewayEventBase;

import com.alibaba.fastjson.JSONObject;

public abstract class AbstractGatewayEvent extends AbstractJsonDeserializable implements GatewayEventBase {
    private final Collection<EventHandler<? extends EventBase>> affected;

    protected final Gateway gateway;

    protected AbstractGatewayEvent(Discord api, JSONObject data) {
        super(api, data);

        gateway = api.getGateway();

        affected = new ArrayList<>();

        affects(api);
        affects(gateway);
    }

    @Override
    public void affects(EventHandler<? extends EventBase> handler) {
        affected.add(handler);
    }

    @Override
    public Collection<EventHandler<? extends EventBase>> getAffected() {
        return Collections.unmodifiableCollection(affected);
    }

    @Override
    public Gateway getGateway() {
        return gateway;
    }
}

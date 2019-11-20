package de.comroid.crystalshard.abstraction.gateway;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import de.comroid.crystalshard.abstraction.serialization.AbstractJsonDeserializable;
import de.comroid.crystalshard.api.Discord;
import de.comroid.crystalshard.api.event.EventBase;
import de.comroid.crystalshard.api.event.EventHandler;
import de.comroid.crystalshard.core.gateway.Gateway;
import de.comroid.crystalshard.core.gateway.event.GatewayEventBase;

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

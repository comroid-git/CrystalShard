package de.comroid.crystalshard.abstraction.gateway;

import de.comroid.crystalshard.abstraction.listener.AbstractListenerManager;
import de.comroid.crystalshard.api.Discord;
import de.comroid.crystalshard.api.listener.model.ListenerAttachable;
import de.comroid.crystalshard.core.gateway.event.GatewayEventBase;
import de.comroid.crystalshard.core.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.gateway.listener.GatewayListenerManager;

public abstract class AbstractGatewayListenerManager<L extends GatewayListener<E>, E extends GatewayEventBase>
        extends AbstractListenerManager<L, E>
        implements GatewayListenerManager<L> {

    protected AbstractGatewayListenerManager(
            Discord api,
            ListenerAttachable<L> attachedTo,
            Class<E> eventClass,
            L listener
    ) {
        super(api, attachedTo, eventClass, listener);
    }
}

package de.comroid.crystalshard.abstraction.gateway;

import de.comroid.crystalshard.abstraction.listener.AbstractListenerManager;
import de.comroid.crystalshard.api.Discord;
import de.comroid.crystalshard.api.listener.model.ListenerAttachable;
import de.comroid.crystalshard.core.api.gateway.event.GatewayEvent;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListenerManager;

public abstract class AbstractGatewayListenerManager<L extends GatewayListener<E>, E extends GatewayEvent>
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

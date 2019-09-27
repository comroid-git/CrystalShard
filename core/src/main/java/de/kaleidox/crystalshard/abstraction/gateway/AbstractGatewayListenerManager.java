package de.kaleidox.crystalshard.abstraction.gateway;

import de.kaleidox.crystalshard.abstraction.listener.AbstractListenerManager;
import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.api.listener.model.ListenerAttachable;
import de.kaleidox.crystalshard.core.api.gateway.event.GatewayEvent;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListener;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListenerManager;

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

package de.comroid.crystalshard.core.api.gateway.listener;

import de.comroid.crystalshard.api.listener.model.AttachableListener;
import de.comroid.crystalshard.api.listener.model.Listener;
import de.comroid.crystalshard.core.api.gateway.event.GatewayEvent;

public interface GatewayListener<E extends GatewayEvent> extends AttachableListener, Listener<E> {
}

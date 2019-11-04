package de.comroid.crystalshard.core.api.gateway.listener;

import de.comroid.crystalshard.api.listener.model.AttachableListener;
import de.comroid.crystalshard.api.listener.model.Listener;
import de.comroid.crystalshard.core.api.gateway.event.GatewayEventBase;

public interface GatewayListener<E extends GatewayEventBase> extends AttachableListener, Listener<E> {
}

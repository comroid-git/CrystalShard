package de.kaleidox.crystalshard.core.api.gateway.listener;

import de.kaleidox.crystalshard.api.listener.AttachableListener;
import de.kaleidox.crystalshard.api.listener.Listener;
import de.kaleidox.crystalshard.core.api.gateway.event.GatewayEvent;

public interface GatewayListener<E extends GatewayEvent> extends AttachableListener, Listener {
}

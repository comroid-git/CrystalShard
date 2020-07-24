package org.comroid.crystalshard.core.gateway.event;

import org.comroid.listnr.EventPayload;

public interface GatewayPayload extends EventPayload {
    GatewayEvent<? extends GatewayPayload> getEventType();
}

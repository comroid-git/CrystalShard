package org.comroid.crystalshard.core.gateway;

import org.comroid.crystalshard.core.event.GatewayEventDefinition;
import org.comroid.crystalshard.core.event.GatewayPayloadWrapper;
import org.comroid.crystalshard.core.gateway.payload.AbstractGatewayPayload;
import org.comroid.listnr.AbstractEventManager;
import org.comroid.listnr.EventManager;
import org.comroid.restless.socket.WebSocket;

public final class Gateway extends AbstractEventManager<
        GatewayPayloadWrapper,
        GatewayEventDefinition<? extends AbstractGatewayPayload>,
        AbstractGatewayPayload
        > {
    private final WebSocket socket;

    public Gateway(WebSocket socket) {
        //noinspection rawtypes
        super((EventManager) socket);

        this.socket = socket;
    }
}

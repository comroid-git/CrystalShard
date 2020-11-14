package org.comroid.crystalshard.gateway;

import org.comroid.api.ContextualProvider;
import org.comroid.common.exception.AssertionException;
import org.comroid.crystalshard.DiscordAPI;
import org.comroid.crystalshard.gateway.event.GatewayEvent;
import org.comroid.mutatio.pipe.Pipe;
import org.comroid.mutatio.pump.Pump;
import org.comroid.restless.socket.WebSocketPacket;
import org.comroid.restless.socket.Websocket;
import org.comroid.uniform.node.UniNode;
import org.jetbrains.annotations.ApiStatus.Internal;

public final class Gateway implements ContextualProvider.Underlying {
    private final Websocket socket;
    private final Pipe<? extends GatewayEvent> eventPipeline;
    private final ContextualProvider context;

    public Pipe<? extends GatewayEvent> getEventPipeline() {
        return eventPipeline;
    }

    @Override
    public ContextualProvider getUnderlyingContextualProvider() {
        return context.plus(this);
    }

    @Internal
    public Gateway(ContextualProvider context, Websocket socket) {
        this.context = context;
        this.socket = socket;
        this.eventPipeline = socket.getPacketPipeline()
                .filter(packet -> packet.getType() == WebSocketPacket.Type.DATA)
                .flatMap(WebSocketPacket::getData)
                .map(DiscordAPI.SERIALIZATION::parse)
                .filter(data -> {
                    if (!OpCode.DISPATCH.test(data))
                        return true;
                    handlePacket(data);
                    return false;
                })
                .map(this::dispatchPacket);

        AssertionException.expect(true, eventPipeline instanceof Pump, "eventPipeline instanceof Pump");
    }

    /**
     * Handles any OPCode that is equal to {@link OpCode#DISPATCH}
     *
     * @param data The packet to handle
     * @return The resulting GatewayEvent
     */
    private GatewayEvent dispatchPacket(UniNode data) {
    }

    /**
     * Handles any other OPCode than {@link OpCode#DISPATCH}
     *
     * @param data The packet to handle
     */
    private void handlePacket(UniNode data) {
    }
}

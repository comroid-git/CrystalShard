package org.comroid.crystalshard.gateway;

import org.comroid.api.ContextualProvider;
import org.comroid.common.exception.AssertionException;
import org.comroid.crystalshard.DiscordAPI;
import org.comroid.crystalshard.gateway.event.GatewayEvent;
import org.comroid.mutatio.pipe.Pipe;
import org.comroid.mutatio.pump.Pump;
import org.comroid.restless.socket.Websocket;
import org.comroid.restless.socket.WebsocketPacket;
import org.comroid.uniform.node.UniNode;
import org.jetbrains.annotations.ApiStatus.Internal;

import java.io.Closeable;
import java.io.IOException;

public final class Gateway implements ContextualProvider.Underlying, Closeable {
    private final Websocket socket;
    private final Pipe<? extends GatewayEvent> eventPipeline;
    private final ContextualProvider context;

    public Pipe<? extends WebsocketPacket> getPacketPipeline() {
        return socket.getPacketPipeline();
    }

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
        this.eventPipeline = getPacketPipeline()
                .filter(packet -> packet.getType() == WebsocketPacket.Type.DATA)
                .flatMap(WebsocketPacket::getData)
                .map(DiscordAPI.SERIALIZATION::parse)
                .yield(OpCode.DISPATCH, this::handlePacket)
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
        return null; // todo
    }

    /**
     * Handles any other OPCode than {@link OpCode#DISPATCH}
     *
     * @param data The packet to handle
     */
    private void handlePacket(UniNode data) {
        // todo
    }

    @Override
    public void close() throws IOException {
        socket.close();
    }

    public void sendHeartbeat() {
        // todo
    }
}

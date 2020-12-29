package org.comroid.crystalshard.gateway;

import org.comroid.api.ContextualProvider;
import org.comroid.api.IntEnum;
import org.comroid.common.exception.AssertionException;
import org.comroid.common.info.MessageSupplier;
import org.comroid.crystalshard.DiscordAPI;
import org.comroid.crystalshard.gateway.event.DispatchEventType;
import org.comroid.crystalshard.gateway.event.GatewayEvent;
import org.comroid.crystalshard.gateway.event.generic.ReadyEvent;
import org.comroid.mutatio.pipe.Pipe;
import org.comroid.mutatio.pump.Pump;
import org.comroid.mutatio.ref.FutureReference;
import org.comroid.mutatio.ref.Reference;
import org.comroid.restless.socket.Websocket;
import org.comroid.restless.socket.WebsocketPacket;
import org.comroid.uniform.ValueType;
import org.comroid.uniform.node.UniNode;
import org.comroid.uniform.node.UniObjectNode;
import org.jetbrains.annotations.ApiStatus.Internal;

import java.io.Closeable;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public final class Gateway implements ContextualProvider.Underlying, Closeable {
    public final @Internal
    Reference<Integer> heartbeatTime = Reference.create();
    public final @Internal
    FutureReference<ReadyEvent> readyEvent;
    private final Websocket socket;
    private final Pipe<? extends UniNode> dataPipeline;
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
        this.dataPipeline = getPacketPipeline()
                .filter(packet -> packet.getType() == WebsocketPacket.Type.DATA)
                .flatMap(WebsocketPacket::getData)
                .map(DiscordAPI.SERIALIZATION::parse);
        this.eventPipeline = dataPipeline
                .yield(OpCode.DISPATCH, this::handlePacket)
                .map(this::dispatchPacket);

        AssertionException.expect(true, eventPipeline instanceof Pump, "eventPipeline instanceof Pump");

        // store latest ready event
        this.readyEvent = new FutureReference<>(getEventPipeline()
                .flatMap(ReadyEvent.class)
                .next());
    }

    public String getSessionID() {
        return readyEvent.flatMap(event -> event.sessionID).assertion();
    }

    /**
     * Handles any OPCode that is equal to {@link OpCode#DISPATCH}
     *
     * @param data The packet to handle
     * @return The resulting GatewayEvent
     */
    private GatewayEvent dispatchPacket(UniNode data) {
        if (!OpCode.DISPATCH.test(data))
            throw new IllegalArgumentException("Cannot dispatch non DISPATCH packet");
        return DispatchEventType.find(data)
                .orElseThrow(() -> new NoSuchElementException("Unknown Dispatch Event: " + data.toString()))
                .createPayload(this, data.get("d").asObjectNode());
    }

    /**
     * Handles any other OPCode than {@link OpCode#DISPATCH}
     *
     * @param data The packet to handle
     */
    private void handlePacket(UniNode data) {
        switch (IntEnum.valueOf(data.get("op").asInt(), OpCode.class)
                .requireNonNull(MessageSupplier.format("Invalid OP Code in data: %s", data))) {
            case DISPATCH:
                throw new IllegalStateException("DISPATCH cannot be handled by packet handler");
            case HEARTBEAT:
                sendHeartbeat().join();
                break;
            case IDENTIFY:
                // client sent only
                break;
            case PRESENCE_UPDATE:
                break;
            case VOICE_STATE_UPDATE:
                break;
            case RESUME:
                break;
            case RECONNECT:
                break;
            case REQUEST_GUILD_MEMBERS:
                break;
            case INVALID_SESSION:
                break;
            case HELLO:
                break;
            case HEARTBEAT_ACK:
                break;
        }
    }

    @Override
    public void close() throws IOException {
        socket.close();
    }

    @Internal
    public CompletableFuture<UniNode> sendHeartbeat() {
        return socket.send(createPayloadBase(OpCode.HEARTBEAT).toString())
                .thenCompose(this::awaitAck);
    }

    @Internal
    public CompletableFuture<ReadyEvent> sendIdentify(int shardID) {
        final UniObjectNode data = createPayloadBase(OpCode.IDENTIFY);

        data.put("shard", ValueType.INTEGER, shardID);

        return socket.send(data.toString())
                .thenCompose(socket -> getEventPipeline().flatMap(ReadyEvent.class).next());
    }

    private CompletableFuture<UniNode> awaitAck(Websocket socket) {
        return CompletableFuture.supplyAsync(() -> {
            CompletableFuture<? extends UniNode> next = dataPipeline
                    .filter(OpCode.HEARTBEAT_ACK)
                    .next();
            UniNode result;

            try {
                result = next.get(heartbeatTime.requireNonNull("Heartbeat Time unavailable") * 2, TimeUnit.MILLISECONDS);
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                throw new RuntimeException("Unexpected Exception awaiting HEARTBEAT_ACK", e);
            }

            return result;
        });
    }

    public UniObjectNode createPayloadBase(OpCode code) {
        return code.apply(DiscordAPI.SERIALIZATION.createUniObjectNode());
    }
}

package org.comroid.crystalshard.gateway;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.comroid.api.ContextualProvider;
import org.comroid.api.IntEnum;
import org.comroid.common.info.MessageSupplier;
import org.comroid.crystalshard.DiscordAPI;
import org.comroid.crystalshard.DiscordBotBase;
import org.comroid.crystalshard.DiscordBotShard;
import org.comroid.crystalshard.gateway.event.DispatchEventType;
import org.comroid.crystalshard.gateway.event.GatewayEvent;
import org.comroid.crystalshard.gateway.event.generic.*;
import org.comroid.crystalshard.gateway.presence.ShardBasedPresence;
import org.comroid.mutatio.model.RefPipe;
import org.comroid.mutatio.ref.Reference;
import org.comroid.mutatio.ref.ReferencePipe;
import org.comroid.restless.socket.Websocket;
import org.comroid.restless.socket.WebsocketPacket;
import org.comroid.uniform.node.UniArrayNode;
import org.comroid.uniform.node.UniNode;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.uniform.node.UniValueNode;
import org.comroid.util.StandardValueType;
import org.jetbrains.annotations.ApiStatus.Internal;

import java.io.Closeable;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.*;

public final class Gateway implements ContextualProvider.Underlying, Closeable {
    private static final Logger logger = LogManager.getLogger();
    @Internal
    public final Reference<Integer> heartbeatTime = Reference.create();
    @Internal
    public final Reference<ReadyEvent> readyEvent;
    @Internal
    public final Reference<ShardBasedPresence> ownPresence;
    @Internal
    private final Reference<Integer> sequence = Reference.create();
    private final Websocket socket;
    private final ReferencePipe<?, ?, WebsocketPacket.Type, UniNode> dataPipeline;
    private final ReferencePipe<?, ?, WebsocketPacket.Type, GatewayEvent> eventPipeline;
    private final DiscordBotShard shard;
    private final Reference<Integer> intents;

    public RefPipe<?, ?, WebsocketPacket.Type, ? extends WebsocketPacket> getPacketPipeline() {
        return socket.getEventPipeline();
    }

    public RefPipe<?, ?, WebsocketPacket.Type, GatewayEvent> getEventPipeline() {
        return eventPipeline;
    }

    @Override
    public ContextualProvider getUnderlyingContextualProvider() {
        return shard.plus(this);
    }

    public Websocket getSocket() {
        return socket;
    }

    public String getSessionID() {
        return readyEvent.flatMap(event -> event.sessionID).assertion();
    }

    @Internal
    public Gateway(DiscordBotShard shard, Websocket socket, int intents) {
        this.shard = shard;
        this.socket = socket;
        this.intents = Reference.constant(intents);
        this.dataPipeline = getPacketPipeline()
                .filter(packet -> packet.getType() == WebsocketPacket.Type.DATA)
                .flatMap(WebsocketPacket::getData)
                .map(DiscordAPI.SERIALIZATION::parse)
                .filter(Objects::nonNull);
        this.eventPipeline = dataPipeline
                .map(data -> {
                    data.wrap("s").map(UniNode::asInt).ifPresent(sequence::set);
                    OpCode op = IntEnum.valueOf(data.get("op").asInt(), OpCode.class)
                            .assertion(MessageSupplier.format("Invalid OP Code in data: %s", data));
                    logger.debug("Attempting to dispatch message as {}: {}", op.getName(), data.toString());
                    return dispatchPacket(data, op);
                })
                .filter(Objects::nonNull)
                .peek(event -> logger.debug("Gateway Event initialization complete [{}]", event.getClass().getSimpleName()));
        // store every latest ready event
        this.readyEvent = Reference.create();
        this.ownPresence = readyEvent.flatMap(ready -> ready.yourself)
                .map(self -> new ShardBasedPresence(shard, self));
        getEventPipeline()
                .flatMap(ReadyEvent.class)
                .peek(ready -> logger.trace("New ReadyEvent received: " + ready))
                .peek(this.readyEvent::set);

        try {
            getEventPipeline()
                    .flatMap(HelloEvent.class)
                    .next()
                    .thenCompose(hello -> {
                        hello.heartbeatInterval.ifPresentOrElse(this::startHeartbeat,
                                () -> logger.warn("Unable to set heartbeat time!"));
                        return sendIdentify(shard.getCurrentShardID());
                    })
                    .join();
        } catch (Throwable t) {
            throw new RuntimeException("Could not send Identify", t);
        }
    }

    private void startHeartbeat(int interval) {
        logger.debug("Shard {} - Started heartbeating at interval: {}", shard.getCurrentShardID(), interval);
        heartbeatTime.set(interval);

        requireFromContext(ScheduledExecutorService.class).scheduleAtFixedRate(
                () -> sendHeartbeat().join(),
                interval,
                interval,
                TimeUnit.MILLISECONDS);
    }

    private GatewayEvent dispatchPacket(UniNode data, OpCode opCode) {
        final UniNode innerData = data.has("d") ? data.get("d") : UniValueNode.NULL;
        switch (opCode) {
            case DISPATCH:
                final DispatchEventType dispatchEventType = DispatchEventType.find(data)
                        .orElseThrow(() -> new NoSuchElementException("Unknown Dispatch Event: " + data.toString()));
                logger.trace("Handling Dispatch event as {} with data {}", dispatchEventType, innerData.toString());
                return dispatchEventType.createPayload(shard, innerData.asObjectNode());
            case IDENTIFY:
            case RESUME:
            case HEARTBEAT:
            case REQUEST_GUILD_MEMBERS:
            case VOICE_STATE_UPDATE:
            case PRESENCE_UPDATE:
                // client sent only, unhandled
                logger.trace("Nothing to do here");
                return null;
            case RECONNECT:
                return new ReconnectEvent(shard, innerData.asObjectNode());
            case INVALID_SESSION:
                final InvalidSessionEvent invalidSessionEvent = new InvalidSessionEvent(shard, innerData);

                try {
                    if (invalidSessionEvent.isResumable()) {
                        logger.warn("Invalid Session received; trying to resume using RESUME...");
                        // reconnect using RESUME
                        sendResume().exceptionally(shard.context.exceptionLogger(logger, Level.ERROR, "Could not Resume"));
                    } else {
                        logger.warn("Invalid Session received; trying to reconnect using IDENTIFY...");
                        // reconnect using IDENTIFY
                        // wait before identify
                        Thread.sleep(1000);
                        sendIdentify(shard.getCurrentShardID())
                                .exceptionally(shard.context.exceptionLogger(logger, Level.ERROR, "Could not Identify"));
                    }
                } catch (Throwable t) {
                    throw new RuntimeException("An Error occurred while reconnecting", t);
                }

                return invalidSessionEvent;
            case HELLO:
                return new HelloEvent(shard, innerData.asObjectNode());
            case HEARTBEAT_ACK:
                return new HeartbeatAckEvent(shard);
        }

        throw new AssertionError("unreachable");
    }

    @Override
    public void close() throws IOException {
        logger.debug("Closing Gateway of Shard {}", shard);
        socket.close();
    }

    @Internal
    public CompletableFuture<UniNode> sendHeartbeat() {
        UniObjectNode obj = createPayloadBase(OpCode.HEARTBEAT);
        obj.put("d", sequence.orElse(0));

        return socket.send(obj.toString()).thenCompose(this::awaitAck);
    }

    @Internal
    public CompletableFuture<ReadyEvent> sendIdentify(int shardID) {
        final UniObjectNode payload = createPayloadBase(OpCode.IDENTIFY);
        UniObjectNode data = payload.putObject("d");

        data.put("token", StandardValueType.STRING, "Bot " + shard.getToken());
        data.put("intents", StandardValueType.INTEGER, intents.assertion());

        UniArrayNode shard = data.putArray("shard");
        shard.put(0, StandardValueType.INTEGER, shardID);
        shard.put(1, StandardValueType.INTEGER, this.shard.getShardCount());

        UniObjectNode prop = data.putObject("properties");
        prop.put("$os", StandardValueType.STRING, System.getProperty("os.name"));
        prop.put("$browser", StandardValueType.STRING, "CrystalShard");
        prop.put("$device", StandardValueType.STRING, this.shard.getFromContext(DiscordBotBase.class)
                .ifPresentMapOrElseGet(bot -> bot.getClass().getSimpleName(), () -> "CrystalShard"));

        return socket.send(payload.toString())
                .thenCombine(getEventPipeline()
                                .flatMap(ReadyEvent.class).next(),
                        (ws, ready) -> ready);
    }

    @Internal
    public CompletableFuture<ResumedEvent> sendResume() {
        final UniObjectNode payload = createPayloadBase(OpCode.RESUME);
        UniObjectNode data = payload.putObject("d");

        data.put("token", "Bot " + shard.getToken());
        data.put("session_id", readyEvent.flatMap(re -> re.sessionID).assertion("No Session ID found"));
        data.put("seq", sequence.assertion("No Sequence number found"));

        return socket.send(payload.toString())
                .thenCombine(getEventPipeline().flatMap(ResumedEvent.class).next(), (ws, event) -> event);
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
        return code.apply(DiscordAPI.SERIALIZATION.createObjectNode());
    }
}

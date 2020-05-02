package org.comroid.crystalshard;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.comroid.crystalshard.core.event.GatewayEvent;
import org.comroid.crystalshard.core.event.GatewayRequestPayload;
import org.comroid.crystalshard.core.net.gateway.CloseCode;
import org.comroid.crystalshard.core.net.rest.DiscordEndpoint;
import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.crystalshard.event.DiscordBotEvent;
import org.comroid.crystalshard.event.DiscordBotEventType;
import org.comroid.crystalshard.model.BotBound;
import org.comroid.dreadpool.ThreadPool;
import org.comroid.listnr.EventAcceptor;
import org.comroid.listnr.EventHub;
import org.comroid.listnr.EventType;
import org.comroid.listnr.ListnrAttachable;
import org.comroid.restless.CommonHeaderNames;
import org.comroid.restless.REST;
import org.comroid.restless.socket.WebSocket;
import org.comroid.uniform.cache.BasicCache;
import org.comroid.uniform.cache.Cache;

import static java.lang.System.currentTimeMillis;
import static org.comroid.crystalshard.CrystalShard.HTTP_ADAPTER;
import static org.comroid.crystalshard.CrystalShard.SERIALIZATION_ADAPTER;

public interface DiscordBot extends ListnrAttachable<GatewayEvent, DiscordBotEvent, DiscordBotEventType, DiscordBotEvent> {
    ThreadPool getThreadPool();

    Cache<Long, Snowflake> getEntityCache();

    REST<DiscordBot> getRestClient();

    default int getShardCount() {
        return getShards().size();
    }

    List<DiscordBot.Shard> getShards();

    DiscordBotEventType.Container getEventTypeContainer();

    static DiscordBot start(String token) {
        if (!SERIALIZATION_ADAPTER.getMimeType()
                .equals("application/json")) {
            throw new IllegalArgumentException("CrystalShard currently only support JSON serialization");
        }

        final GatewayRequestPayload suggested = new REST<>(HTTP_ADAPTER,
                SERIALIZATION_ADAPTER
        ).request(GatewayRequestPayload.Basic.class)
                .url(DiscordEndpoint.GATEWAY_BOT.make())
                .addHeader(CommonHeaderNames.AUTHORIZATION, "Bot " + token)
                .addHeader(CommonHeaderNames.REQUEST_CONTENT_TYPE, SERIALIZATION_ADAPTER.getMimeType())
                .method(REST.Method.GET)
                .execute$deserializeSingle()
                .join();

        return new Support.ShardingManager("Bot " + token,
                Math.max(1, suggested.getShardCount()),
                new ThreadGroup(CrystalShard.THREAD_GROUP, "Bot#" + currentTimeMillis()),
                suggested.getGatewayUri()
        );
    }

    interface Shard extends BotBound {
        int getShardID();

        <T> WebSocket<T> getWebSocket();
    }

    final class Support {
        private static final class ShardingManager implements DiscordBot {
            private final String                                  token;
            private final ThreadPool                              threadPool;
            private final Cache<Long, Snowflake>                  entityCache;
            private final REST<DiscordBot>                        restClient;
            private final List<Shard>                             shards;
            private final List<WebSocket<GatewayEvent>>           webSockets;
            private final EventHub<GatewayEvent, DiscordBotEvent> mainEventHub;
            private final DiscordBotEventType.Container           eventTypeContainer;

            private ShardingManager(String token, int shards, ThreadGroup threadGroup, URI gatewayUri) {
                this.token       = token;
                this.threadPool  = ThreadPool.fixedSize(threadGroup, 8 * shards);
                this.entityCache = new BasicCache<>(500);
                this.restClient  = new REST<>(HTTP_ADAPTER, SERIALIZATION_ADAPTER, this);

                var socketHeaders = new WebSocket.Header.List().add("Authorization", token)
                        .add("Content-Type", SERIALIZATION_ADAPTER.getMimeType());

                List<WebSocket<GatewayEvent>>                                                          sockets
                                                                                                                 =
                        new ArrayList<>();
                List<EventHub<DiscordBotEvent, DiscordBotEvent, GatewayEventType, DiscordBotEvent>> eventHubs
                                                                                                                 =
                        new ArrayList<>();
                this.webSockets = IntStream.range(0, shards)
                        .mapToObj(shardId -> {
                            WebSocket<GatewayEvent> webSocket = HTTP_ADAPTER.createWebSocket(SERIALIZATION_ADAPTER,
                                    socketHeaders,
                                    threadPool,
                                    gatewayUri,
                                    this::preprocessWebsocketData
                            )
                                    .join();
                            webSocket.setCloseCodeResolver(CloseCode::toString);

                            webSocket.getEventHub()

                            return webSocket;
                        })
                        .collect(Collectors.toUnmodifiableList());

                this.mainEventHub = new EventHub<>(threadPool, this::preprocessEventData);
                final EventAcceptor<EventType<GatewayEvent, String, GatewayEvent>, GatewayEvent> webSocketMessageForwarder
                        = new EventAcceptor.Support.Abstract<>() {
                    @Override
                    public <T extends GatewayEvent> void acceptEvent(T eventPayload) {
                        mainEventHub.publish(eventPayload);
                    }

                    @Override
                    public boolean canAccept(EventType<GatewayEvent, ?, ?> eventType) {
                        return true;
                    }
                };
                webSockets.stream()
                        .map(WebSocket::getEventHub)
                        .forEach(hub -> hub.registerAcceptor(webSocketMessageForwarder));

                this.eventTypeContainer = new DiscordBotEventType.Container(this);

                this.shards = IntStream.range(0, shards)
                        .mapToObj(id -> new ShardImpl(this, id, webSockets.get(id)))
                        .collect(Collectors.toUnmodifiableList());
            }

            private GatewayEvent preprocessWebsocketData(String s) {
            }

            private DiscordBotEvent preprocessEventData(GatewayEvent gatewayRequestPayload) {
            }

            @Override
            public ThreadPool getThreadPool() {
                return threadPool;
            }

            @Override
            public Cache<Long, Snowflake> getEntityCache() {
                return entityCache;
            }

            @Override
            public REST<DiscordBot> getRestClient() {
                return restClient;
            }

            @Override
            public List<Shard> getShards() {
                return shards;
            }

            @Override
            public DiscordBotEventType.Container getEventTypeContainer() {
                return eventTypeContainer;
            }

            @Override
            public EventHub<GatewayEvent, DiscordBotEvent> getEventHub() {
                return mainEventHub;
            }

            @Override
            public DiscordBotEventType getBaseEventType() {
                return eventTypeContainer.TYPE_BASE;
            }
        }

        private static final class ShardImpl implements Shard {
            private final ShardingManager         shardingManager;
            private final int                     shardId;
            private final WebSocket<GatewayEvent> webSocket;

            public ShardImpl(
                    ShardingManager shardingManager, int shardId, WebSocket<GatewayEvent> webSocket
            ) {
                this.shardingManager = shardingManager;
                this.shardId         = shardId;
                this.webSocket       = webSocket;
            }

            @Override
            public int getShardID() {
                return shardId;
            }

            @Override
            public WebSocket<?> getWebSocket() {
                return webSocket;
            }

            @Override
            public DiscordBot getBot() {
                return shardingManager;
            }
        }
    }
}

package org.comroid.crystalshard;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.comroid.crystalshard.core.event.GatewayPayload;
import org.comroid.crystalshard.core.net.rest.DiscordEndpoint;
import org.comroid.crystalshard.core.net.socket.GatewayCloseCode;
import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.crystalshard.event.DiscordBotEvent;
import org.comroid.crystalshard.event.DiscordBotEventType;
import org.comroid.crystalshard.model.BotBound;
import org.comroid.dreadpool.ThreadPool;
import org.comroid.listnr.EventHub;
import org.comroid.listnr.ListnrAttachable;
import org.comroid.restless.CommonHeaderNames;
import org.comroid.restless.REST;
import org.comroid.restless.socket.WebSocket;
import org.comroid.uniform.cache.BasicCache;
import org.comroid.uniform.cache.Cache;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.varbind.VarCarrier;
import org.comroid.varbind.VariableCarrier;

import static java.lang.System.currentTimeMillis;
import static org.comroid.crystalshard.CrystalShard.HTTP_ADAPTER;
import static org.comroid.crystalshard.CrystalShard.SERIALIZATION_ADAPTER;

public interface DiscordBot
        extends ListnrAttachable<UniObjectNode, VarCarrier<DiscordBot>, DiscordBotEventType<DiscordBotEvent>, DiscordBotEvent> {
    String getToken();

    ThreadPool getThreadPool();

    WebSocket<?> getWebSocket();

    Cache<Long, Snowflake> getEntityCache();

    REST<DiscordBot> getRestClient();

    default int getShardCount() {
        return getShards().size();
    }

    List<DiscordBot.Shard> getShards();

    static DiscordBot start(String token) {
        final GatewayPayload suggested = new REST<>(HTTP_ADAPTER, SERIALIZATION_ADAPTER).request(GatewayPayload.Basic.class)
                .url(DiscordEndpoint.GATEWAY_BOT.make())
                .addHeader(CommonHeaderNames.AUTHORIZATION, "Bot " + token)
                .addHeader(CommonHeaderNames.REQUEST_CONTENT_TYPE, SERIALIZATION_ADAPTER.getMimeType())
                .method(REST.Method.GET)
                .execute$deserializeSingle()
                .join();

        return new Support.Impl(
                "Bot " + token,
                Math.max(1, suggested.getShardCount()),
                new ThreadGroup(CrystalShard.THREAD_GROUP, "Bot#" + currentTimeMillis()),
                suggested.getGatewayUri()
        );
    }

    interface Shard extends BotBound {
        int getShardID();
    }

    final class Support {
        private static final class Impl implements DiscordBot {
            private final String                                          token;
            private final ThreadPool                                      threadPool;
            private final Cache<Long, Snowflake>                          entityCache;
            private final WebSocket<GatewayPayload>                       webSocket;
            private final EventHub<GatewayPayload, DiscordBotEvent> eventHub;
            private final DiscordBotEventType.Container                   eventContainer;
            private final REST<DiscordBot>                                restClient;
            private final List<Shard>                                     shards;

            private Impl(String token, int shardCount, ThreadGroup group, URI websocketUri) {
                this.token       = token;
                this.threadPool  = ThreadPool.fixedSize(group, 8 * shardCount);
                this.entityCache = new BasicCache<>(500);

                final WebSocket.Header.List socketHeaders = new WebSocket.Header.List();
                this.webSocket      = HTTP_ADAPTER.createWebSocket(SERIALIZATION_ADAPTER,
                        socketHeaders,
                        threadPool,
                        websocketUri,
                        this::preprocessWebsocketData
                )
                        .thenApply(socket -> {
                            socket.setCloseCodeResolver(GatewayCloseCode::toString);

                            return socket;
                        })
                        .join();
                this.eventHub       = webSocket.getEventHub()
                        .dependentHub(threadPool, gatewayEvent -> null/* todo: Implement gateway event types */);
                this.eventContainer = new DiscordBotEventType.Container(this);
                this.restClient     = new REST<>(HTTP_ADAPTER, SERIALIZATION_ADAPTER, this);
                this.shards         = IntStream.range(0, shardCount)
                        .mapToObj(it -> new ShardImpl(this, it))
                        .collect(Collectors.toUnmodifiableList());
            }

            private GatewayPayload preprocessWebsocketData(String data) {
                return null; //todo
            }

            @Override
            public String getToken() {
                return token;
            }

            @Override
            public ThreadPool getThreadPool() {
                return threadPool;
            }

            @Override
            public WebSocket<?> getWebSocket() {
                return webSocket;
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
            public EventHub<UniObjectNode, VarCarrier<DiscordBot>> getEventHub() {
                return eventHub;
            }

            @Override
            public DiscordBotEventType<DiscordBotEvent> getBaseEventType() {
                return eventContainer.TYPE_BASE;
            }
        }

        private static final class ShardImpl implements Shard {
            private final DiscordBot bot;
            private final int        id;

            public ShardImpl(DiscordBot bot, int myId) {
                this.bot = bot;
                this.id  = myId;
            }

            @Override
            public int getShardID() {
                return id;
            }

            @Override
            public DiscordBot getBot() {
                return bot;
            }
        }
    }
}

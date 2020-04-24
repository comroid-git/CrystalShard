package org.comroid.crystalshard;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.comroid.crystalshard.core.event.BotGatewayEvent;
import org.comroid.crystalshard.core.net.DiscordEndpoint;
import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.crystalshard.model.BotBound;
import org.comroid.dreadpool.ThreadPool;
import org.comroid.listnr.ListnrAttachable;
import org.comroid.restless.CommonHeaderNames;
import org.comroid.restless.REST;
import org.comroid.restless.socket.WebSocket;
import org.comroid.uniform.cache.BasicCache;
import org.comroid.uniform.cache.Cache;
import org.comroid.uniform.node.UniObjectNode;

import static java.lang.System.currentTimeMillis;

public interface DiscordBot extends ListnrAttachable<?, UniObjectNode,> {
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
        final BotGatewayEvent suggested = new REST<DiscordBot>(CrystalShard.HTTP_ADAPTER,
                CrystalShard.SERIALIZATION_ADAPTER
        ).request(BotGatewayEvent.class)
                .url(DiscordEndpoint.GATEWAY_BOT.make())
                .addHeader(CommonHeaderNames.AUTHORIZATION, "Bot " + token)
                .addHeader(CommonHeaderNames.REQUEST_CONTENT_TYPE, CrystalShard.SERIALIZATION_ADAPTER.getMimeType())
                .method(REST.Method.GET)
                .execute$deserializeSingle()
                .join();

        return new Support.Impl("Bot " + token,
                Math.max(1, suggested.getShardCount()),
                new ThreadGroup(CrystalShard.THREAD_GROUP, "Bot#" + currentTimeMillis()),
                suggested.getGatewayUrl()
        );
    }

    interface Shard extends BotBound {
        int getShardID();
    }

    final class Support {
        private static final class Impl implements DiscordBot {
            private final String                 token;
            private final ThreadPool             threadPool;
            private final Cache<Long, Snowflake> entityCache;
            private final WebSocket<?>           webSocket;
            private final REST<DiscordBot>       restClient;
            private final List<Shard>            shards;

            private Impl(String token, int shardCount, ThreadGroup group, URI websocketUri) {
                this.token       = token;
                this.threadPool  = ThreadPool.fixedSize(group, 8 * shardCount);
                this.entityCache = new BasicCache<>(500);
                this.webSocket   = CrystalShard.HTTP_ADAPTER.createWebSocket(CrystalShard.SERIALIZATION_ADAPTER,
                        new WebSocket.Header.List(),
                        threadPool,
                        websocketUri
                )
                        .join();
                this.restClient  = new REST<>(CrystalShard.HTTP_ADAPTER, CrystalShard.SERIALIZATION_ADAPTER, this);
                this.shards      = IntStream.range(0, shardCount)
                        .mapToObj(it -> new ShardImpl(this, it))
                        .collect(Collectors.toUnmodifiableList());
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

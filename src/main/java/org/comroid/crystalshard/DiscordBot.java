package org.comroid.crystalshard;

import org.comroid.common.func.Processor;
import org.comroid.common.ref.Reference;
import org.comroid.crystalshard.core.event.GatewayEvent;
import org.comroid.crystalshard.core.event.GatewayRequestPayload;
import org.comroid.crystalshard.core.net.gateway.CloseCode;
import org.comroid.crystalshard.core.net.rest.DiscordEndpoint;
import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.crystalshard.entity.channel.*;
import org.comroid.crystalshard.entity.user.User;
import org.comroid.crystalshard.event.DiscordBotEvent;
import org.comroid.crystalshard.event.DiscordBotEventType;
import org.comroid.crystalshard.model.BotBound;
import org.comroid.crystalshard.model.user.UserPresence;
import org.comroid.crystalshard.voice.VoiceState;
import org.comroid.dreadpool.ThreadPool;
import org.comroid.listnr.EventType;
import org.comroid.restless.CommonHeaderNames;
import org.comroid.restless.REST;
import org.comroid.restless.socket.WebSocket;
import org.comroid.uniform.cache.BasicCache;
import org.comroid.uniform.cache.Cache;
import org.comroid.uniform.cache.FileCache;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.container.DataContainer;
import org.jetbrains.annotations.ApiStatus.Internal;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.System.currentTimeMillis;
import static org.comroid.crystalshard.CrystalShard.HTTP_ADAPTER;
import static org.comroid.crystalshard.CrystalShard.SERIALIZATION_ADAPTER;

public interface DiscordBot extends ListnrAttachable<GatewayEvent, DiscordBotEvent, DiscordBotEventType, DiscordBotEvent> {
    String getToken();

    ThreadPool getThreadPool();

    FileCache<Long, Snowflake, DiscordBot> getEntityCache();

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

        final GatewayRequestPayload suggested = new REST<>(
                HTTP_ADAPTER,
                SERIALIZATION_ADAPTER
        ).request(GatewayRequestPayload.Bind.Root)
                .endpoint(DiscordEndpoint.GATEWAY_BOT)
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

    @Internal
    UserPresence updatePresence(UniObjectNode data);

    @Internal
    VoiceState updateVoiceState(UniObjectNode data);

    default <T extends DataContainer<DiscordBot>> REST<DiscordBot>.Request<T> request(Class<T> type) {
        return getRestClient().request(type)
                .addHeader(CommonHeaderNames.AUTHORIZATION, "Bot " + getToken())
                .addHeader(CommonHeaderNames.REQUEST_CONTENT_TYPE, SERIALIZATION_ADAPTER.getMimeType());
    }

    default <T extends DataContainer<DiscordBot>> REST<DiscordBot>.Request<T> request(GroupBind<T, DiscordBot> type) {
        return getRestClient().request(type)
                .addHeader(CommonHeaderNames.AUTHORIZATION, "Bot " + getToken())
                .addHeader(CommonHeaderNames.REQUEST_CONTENT_TYPE, SERIALIZATION_ADAPTER.getMimeType());
    }

    default Processor<? extends Snowflake> getSnowflakeByID(long id) {
        return Processor.providedOptional(() -> getEntityCache()
                .stream(other -> id == other)
                .findAny())
                .flatMap(Reference::wrap);
    }

    default Processor<User> getUserByID(long id) {
        return getSnowflakeByID(id).map(User.class::cast);
    }

    default Processor<Channel> getChannelByID(long id) {
        return getSnowflakeByID(id).map(Channel.class::cast);
    }

    default Processor<TextChannel> getTextChannelByID(long id) {
        return getChannelByID(id).flatMap(Channel::asTextChannel);
    }

    default Processor<VoiceChannel> getVoiceChannelByID(long id) {
        return getChannelByID(id).flatMap(Channel::asVoiceChannel);
    }

    default Processor<GuildChannel> getGuildChannelByID(long id) {
        return getChannelByID(id).flatMap(Channel::asGuildChannel);
    }

    default Processor<GuildTextChannel> getGuildTextChannelByID(long id) {
        return getChannelByID(id).flatMap(Channel::asGuildTextChannel);
    }

    default Processor<GuildVoiceChannel> getGuildVoiceChannelByID(long id) {
        return getChannelByID(id).flatMap(Channel::asGuildVoiceChannel);
    }

    default Processor<PrivateChannel> getPrivateChannelByID(long id) {
        return getChannelByID(id).flatMap(Channel::asPrivateChannel);
    }

    default Processor<PrivateTextChannel> getPrivateTextChannelByID(long id) {
        return getChannelByID(id).flatMap(Channel::asPrivateTextChannel);
    }

    interface Shard extends BotBound {
        int getShardID();

        <T> WebSocket<T> getWebSocket();
    }

    final class Support {
        private static final class ShardingManager implements DiscordBot {
            private final String token;
            private final ThreadPool threadPool;
            private final Cache<Long, Snowflake> entityCache;
            private final REST<DiscordBot> restClient;
            private final List<Shard> shards;
            private final List<WebSocket<GatewayEvent>> webSockets;
            private final EventHub<GatewayEvent, DiscordBotEvent> mainEventHub;
            private final DiscordBotEventType.Container eventTypeContainer;

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

            private ShardingManager(String token, int shards, ThreadGroup threadGroup, URI gatewayUri) {
                this.token = token;
                this.threadPool = ThreadPool.fixedSize(threadGroup, 8 * shards);
                this.entityCache = new BasicCache<>(500);
                this.restClient = new REST<>(HTTP_ADAPTER, SERIALIZATION_ADAPTER, this);

                var socketHeaders = new WebSocket.Header.List().add("Authorization", token)
                        .add("Content-Type", SERIALIZATION_ADAPTER.getMimeType());

                List<WebSocket<GatewayEvent>> sockets
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
        }

        private static final class ShardImpl implements Shard {
            private final ShardingManager shardingManager;
            private final int shardId;
            private final WebSocket<GatewayEvent> webSocket;

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

            public ShardImpl(
                    ShardingManager shardingManager, int shardId, WebSocket<GatewayEvent> webSocket
            ) {
                this.shardingManager = shardingManager;
                this.shardId = shardId;
                this.webSocket = webSocket;
            }
        }
    }
}

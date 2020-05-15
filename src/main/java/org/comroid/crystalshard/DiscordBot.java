package org.comroid.crystalshard;

import org.comroid.common.func.Processor;
import org.comroid.common.ref.Reference;
import org.comroid.common.util.Bitmask;
import org.comroid.crystalshard.core.cache.SnowflakeCache;
import org.comroid.crystalshard.core.cache.SnowflakeSelector;
import org.comroid.crystalshard.core.event.GatewayEvent;
import org.comroid.crystalshard.core.event.GatewayRequestPayload;
import org.comroid.crystalshard.core.net.gateway.CloseCode;
import org.comroid.crystalshard.core.net.rest.DiscordEndpoint;
import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.crystalshard.entity.channel.*;
import org.comroid.crystalshard.entity.guild.Guild;
import org.comroid.crystalshard.entity.message.Message;
import org.comroid.crystalshard.entity.user.User;
import org.comroid.crystalshard.model.BotBound;
import org.comroid.crystalshard.model.channel.PermissionOverride;
import org.comroid.crystalshard.model.user.UserPresence;
import org.comroid.crystalshard.voice.VoiceState;
import org.comroid.dreadpool.ThreadPool;
import org.comroid.listnr.model.EventType;
import org.comroid.restless.CommonHeaderNames;
import org.comroid.restless.REST;
import org.comroid.restless.socket.WebSocket;
import org.comroid.uniform.cache.BasicCache;
import org.comroid.uniform.cache.Cache;
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

public interface DiscordBot {
    String getToken();

    ThreadPool getThreadPool();

    SnowflakeCache getCache();

    REST<DiscordBot> getRestClient();

    default int getShardCount() {
        return getShards().size();
    }

    List<DiscordBot.Shard> getShards();

    List<DiscordAPI.Intent> getIntents();

    /**
     * @deprecated Intents are mandatory in a future Discord API release
     * @param token The token of the bot
     * @return A running bot instance
     */
    @Deprecated
    static DiscordBot start(String token) {
        return start(token, Bitmask.combine(DiscordAPI.Intent.values()));
    }

    static DiscordBot start(String token, int intents) {
        if (!SERIALIZATION_ADAPTER.getMimeType()
                .equals("application/json")) {
            throw new IllegalArgumentException("CrystalShard currently only support JSON serialization");
        }

        final GatewayRequestPayload grp = new REST<>(
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
                intents,
                Math.max(1, grp.getShardCount()),
                new ThreadGroup(CrystalShard.THREAD_GROUP, "Bot#" + currentTimeMillis()),
                grp.getGatewayUri()
        );
    }

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

    default Processor<SnowflakeSelector> getSnowflakesByID(long id) {
        return Reference.provided(() -> getCache()
                .stream(other -> id == other)
                .findAny())
                .process()
                .flatMap(ref -> ref)
                .flatMap(Reference::wrap);
    }

    default Processor<Guild> getGuildByID(long id) {
        return getSnowflakesByID(id)
                .map(SnowflakeSelector::asGuild);
    }

    default Processor<User> getUserByID(long id) {
        return getSnowflakesByID(id).map(SnowflakeSelector::asUser);
    }

    default Processor<Channel> getChannelByID(long id) {
        return getSnowflakesByID(id).map(SnowflakeSelector::asChannel);
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

    default Processor<ChannelCategory> getChannelCategoryByID(long id) {
        return getChannelByID(id).flatMap(Channel::asChannelCategory);
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

    default Processor<Message> getMessageByID(long id) {
        return getSnowflakesByID(id).map(SnowflakeSelector::asMessage);
    }

    @Internal
    UserPresence updatePresence(UniObjectNode data);

    @Internal
    VoiceState updateVoiceState(UniObjectNode data);

    @Internal
    User updateUser(UniObjectNode data);

    @Internal
    PermissionOverride makeOverwrite(UniObjectNode data);

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
            private final int intent;

            @Override
            public ThreadPool getThreadPool() {
                return threadPool;
            }

            @Override
            public Cache<Long, Snowflake> getCache() {
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

            private ShardingManager(String token, int intent, int shards, ThreadGroup threadGroup, URI gatewayUri) {
                this.token = token;
                this.intent = intent;
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

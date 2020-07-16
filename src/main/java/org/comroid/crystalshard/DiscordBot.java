package org.comroid.crystalshard;

import org.comroid.crystalshard.core.event.GatewayEventDefinition;
import org.comroid.crystalshard.core.event.GatewayPayloadWrapper;
import org.comroid.crystalshard.core.gateway.payload.AbstractGatewayPayload;
import org.comroid.crystalshard.core.rest.payload.GatewayBotRequestPayload;
import org.comroid.crystalshard.core.rest.DiscordEndpoint;
import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.crystalshard.entity.Snowflake.Type;
import org.comroid.crystalshard.entity.channel.Channel;
import org.comroid.crystalshard.entity.guild.GuildMember;
import org.comroid.crystalshard.entity.user.User;
import org.comroid.crystalshard.event.DiscordBotEvent;
import org.comroid.crystalshard.event.DiscordBotPayload;
import org.comroid.crystalshard.model.BotBound;
import org.comroid.crystalshard.model.channel.PermissionOverride;
import org.comroid.crystalshard.model.emoji.Emoji;
import org.comroid.crystalshard.model.message.MessageActivity;
import org.comroid.crystalshard.model.message.MessageApplication;
import org.comroid.crystalshard.model.message.MessageReference;
import org.comroid.crystalshard.model.user.UserPresence;
import org.comroid.crystalshard.voice.VoiceState;
import org.comroid.listnr.AbstractEventManager;
import org.comroid.listnr.EventManager;
import org.comroid.matrix.Matrix2;
import org.comroid.mutatio.proc.Processor;
import org.comroid.mutatio.ref.FutureReference;
import org.comroid.mutatio.ref.Reference;
import org.comroid.mutatio.span.Span;
import org.comroid.restless.CommonHeaderNames;
import org.comroid.restless.REST;
import org.comroid.restless.socket.WebSocket;
import org.comroid.restless.socket.event.WebSocketEvent;
import org.comroid.restless.socket.event.WebSocketPayload;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.util.Bitmask;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.container.DataContainer;
import org.jetbrains.annotations.ApiStatus.Internal;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.comroid.crystalshard.CrystalShard.HTTP_ADAPTER;
import static org.comroid.crystalshard.CrystalShard.SERIALIZATION_ADAPTER;

public interface DiscordBot extends EventManager<GatewayPayloadWrapper, DiscordBotEvent, DiscordBotPayload> {
    String getToken();

    ScheduledExecutorService getExecutorService();

    Matrix2<Long, Type<? extends Snowflake>, ? extends Snowflake> getCache();

    REST<DiscordBot> getRestClient();

    default int getShardCount() {
        return getShards().size();
    }

    Processor<User> getYourself();

    Processor<User> getBotOwner();

    List<DiscordBot.Shard> getShards();

    Collection<DiscordAPI.Intent> getIntents();

    default int getIntentAsInteger() {
        return getIntents().stream()
                .map(DiscordAPI.Intent::getValue)
                .collect(Bitmask.collector());
    }

    static DiscordBot start(String token) {
        if (!SERIALIZATION_ADAPTER.getMimeType().equals("application/json")) {
            throw new IllegalArgumentException("CrystalShard currently only supports JSON serialization");
        }

        final GatewayBotRequestPayload grp = new REST<>(
                HTTP_ADAPTER,
                SERIALIZATION_ADAPTER,
                (DiscordBot) null
        ).request(GatewayBotRequestPayload.Root)
                .endpoint(DiscordEndpoint.GATEWAY_BOT)
                .addHeader(CommonHeaderNames.AUTHORIZATION, "Bot " + token)
                .addHeader(CommonHeaderNames.REQUEST_CONTENT_TYPE, SERIALIZATION_ADAPTER.getMimeType())
                .method(REST.Method.GET)
                .execute$deserializeSingle()
                .join();

        final REST.Header.List socketHeaders = new REST.Header.List();
        socketHeaders.add("Authorization", token);
        socketHeaders.add("Content-Type", SERIALIZATION_ADAPTER.getMimeType());

        final int shards = grp.getRecommendedShardCount();
        final FutureReference<Support.ShardingManager> managerRef = new FutureReference<>();
        final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(8);

        final List<? extends WebSocket> webSockets = Collections.unmodifiableList(IntStream.range(0, shards)
                .mapToObj(shardId -> CrystalShard.createGateway(
                        SERIALIZATION_ADAPTER,
                        executorService,
                        grp.getWebSocketURI(),
                        socketHeaders))
                .map(connection -> connection.thenComposeAsync(webSocket -> {
                    webSocket.eventPipe(WebSocketEvent.DATA)
                            .filter(GatewayEventDefinition.HELLO.)
                }))
                .map(CompletableFuture::join)
                .collect(Collectors.toList()));
        final List<Shard> allShards = Collections.unmodifiableList(IntStream.range(0, shards)
                .mapToObj(id -> new Support.ShardImpl(managerRef, id, webSockets.get(id)))
                .collect(Collectors.toList()));

        final Support.ShardingManager shardingManager = new Support.ShardingManager(
                "Bot " + token,
                executorService,
                allShards
        );
        managerRef.future.complete(shardingManager);

        return shardingManager;
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

    default <T extends Snowflake> Processor<T> getSnowflake(Type<T> type, long id) {
        return Processor.ofReference(Reference.provided(() -> getCache().get(id, type)))
                .filter(type.getTypeClass()::isInstance)
                .map(type.getTypeClass()::cast);
    }

    @Internal
    UserPresence updatePresence(UniObjectNode data);

    @Internal
    VoiceState updateVoiceState(UniObjectNode data);

    @Internal
    User updateUser(UniObjectNode data);

    @Internal
    GuildMember updateGuildMember(UniObjectNode data);

    @Internal
    Emoji updateEmoji(UniObjectNode data);

    @Internal
    PermissionOverride makeOverwrite(UniObjectNode data);

    @Internal
    Processor<Channel> resolveChannelMention(UniObjectNode data);

    @Internal
    Processor<MessageActivity> resolveMessageActivity(UniObjectNode data);

    @Internal
    Processor<MessageApplication> resolveMessageApplication(UniObjectNode data);

    @Internal
    Processor<MessageReference> resolveMessageReference(UniObjectNode data);

    interface Shard extends BotBound {
        int getShardID();

        WebSocket getWebSocket();
    }

    final class Support {
        private static final class ShardingManager
                extends AbstractEventManager<GatewayPayloadWrapper, DiscordBotEvent, DiscordBotPayload>
                implements DiscordBot {
            private final Matrix2<Long, Type<? extends Snowflake>, ? extends Snowflake> entityCache;
            //todo Intent usage
            private final Span<DiscordAPI.Intent> activeIntents
                    = Span.<DiscordAPI.Intent>make().initialValues(DiscordAPI.Intent.values()).span();
            private final ScheduledExecutorService executorService;
            private final REST<DiscordBot> restClient;
            private final List<Shard> shards;
            private final String token;

            @Override
            public String getToken() {
                return token;
            }

            @Override
            public ScheduledExecutorService getExecutorService() {
                return executorService;
            }

            @Override
            public Matrix2<Long, Type<? extends Snowflake>, ? extends Snowflake> getCache() {
                return entityCache;
            }

            @Override
            public REST<DiscordBot> getRestClient() {
                return restClient;
            }

            @Override
            public Processor<User> getYourself() {
                return null; //todo
            }

            @Override
            public Processor<User> getBotOwner() {
                return null; //todo
            }

            @Override
            public List<Shard> getShards() {
                return shards;
            }

            @Override
            public Collection<DiscordAPI.Intent> getIntents() {
                return Collections.unmodifiableCollection(activeIntents);
            }

            private ShardingManager(
                    String token,
                    ScheduledExecutorService executorService,
                    List<Shard> allShards
            ) {
                //noinspection unchecked
                super(allShards.stream()
                        .map(Shard::getWebSocket)
                        .toArray(EventManager[]::new));

                this.token = token;
                this.entityCache = Matrix2.create();
                this.executorService = executorService;
                this.restClient = new REST<>(HTTP_ADAPTER, SERIALIZATION_ADAPTER, this);
                this.shards = allShards;
            }

            @Override
            public UserPresence updatePresence(UniObjectNode data) {
                return null;
            }

            @Override
            public VoiceState updateVoiceState(UniObjectNode data) {
                return null;
            }

            @Override
            public User updateUser(UniObjectNode data) {
                return null;
            }

            @Override
            public GuildMember updateGuildMember(UniObjectNode data) {
                return null;
            }

            @Override
            public Emoji updateEmoji(UniObjectNode data) {
                return null;
            }

            @Override
            public PermissionOverride makeOverwrite(UniObjectNode data) {
                return null;
            }

            @Override
            public Processor<Channel> resolveChannelMention(UniObjectNode data) {
                return null;
            }

            @Override
            public Processor<MessageActivity> resolveMessageActivity(UniObjectNode data) {
                return null;
            }

            @Override
            public Processor<MessageApplication> resolveMessageApplication(UniObjectNode data) {
                return null;
            }

            @Override
            public Processor<MessageReference> resolveMessageReference(UniObjectNode data) {
                return null;
            }
        }

        private static final class ShardImpl
                extends AbstractEventManager<WebSocketPayload.Data, GatewayEventDefinition<? extends AbstractGatewayPayload>, AbstractGatewayPayload>
                implements Shard {
            private final FutureReference<ShardingManager> shardingManager;
            private final int shardId;
            private final WebSocket webSocket;

            @Override
            public int getShardID() {
                return shardId;
            }

            @Override
            public WebSocket getWebSocket() {
                return webSocket;
            }

            @Override
            public DiscordBot getBot() {
                return shardingManager.requireNonNull();
            }

            public ShardImpl(
                    FutureReference<ShardingManager> shardingManager, int shardId, WebSocket webSocket
            ) {
                super(webSocket);

                this.shardingManager = shardingManager;
                this.shardId = shardId;
                this.webSocket = webSocket;
            }
        }
    }
}

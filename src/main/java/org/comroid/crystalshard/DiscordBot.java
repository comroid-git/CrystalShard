package org.comroid.crystalshard;

import org.comroid.crystalshard.core.cache.SnowflakeCache;
import org.comroid.crystalshard.core.event.GatewayRequestPayload;
import org.comroid.crystalshard.core.net.rest.DiscordEndpoint;
import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.crystalshard.entity.channel.*;
import org.comroid.crystalshard.entity.guild.Guild;
import org.comroid.crystalshard.entity.guild.GuildMember;
import org.comroid.crystalshard.entity.guild.Role;
import org.comroid.crystalshard.entity.message.Message;
import org.comroid.crystalshard.entity.user.User;
import org.comroid.crystalshard.entity.webhook.Webhook;
import org.comroid.crystalshard.model.BotBound;
import org.comroid.crystalshard.model.channel.PermissionOverride;
import org.comroid.crystalshard.model.emoji.Emoji;
import org.comroid.crystalshard.model.message.MessageActivity;
import org.comroid.crystalshard.model.message.MessageApplication;
import org.comroid.crystalshard.model.message.MessageReference;
import org.comroid.crystalshard.model.user.UserPresence;
import org.comroid.crystalshard.voice.VoiceState;
import org.comroid.dreadpool.ThreadPool;
import org.comroid.mutatio.proc.Processor;
import org.comroid.mutatio.ref.Reference;
import org.comroid.restless.CommonHeaderNames;
import org.comroid.restless.REST;
import org.comroid.restless.socket.WebSocket;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.util.Bitmask;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.container.DataContainer;
import org.jetbrains.annotations.ApiStatus.Internal;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.System.currentTimeMillis;
import static org.comroid.crystalshard.CrystalShard.HTTP_ADAPTER;
import static org.comroid.crystalshard.CrystalShard.SERIALIZATION_ADAPTER;

public interface DiscordBot {
    String getToken();

    ThreadPool getThreadPool();

    <F extends Snowflake> SnowflakeCache<F> getCache(Class<F> type);

    REST<DiscordBot> getRestClient();

    default int getShardCount() {
        return getShards().size();
    }

    List<DiscordBot.Shard> getShards();

    List<DiscordAPI.Intent> getIntents();

    /**
     * @param token The token of the bot
     * @return A running bot instance
     * @deprecated Intents are mandatory in a future Discord API release
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
                .findAny()
                .orElse(null))
                .process()
                .flatMap(Function.identity());
    }

    default Processor<Guild> getGuildByID(long id) {
        return getSnowflakesByID(id)
                .map(SnowflakeSelector::asGuild);
    }

    default Processor<Webhook> getWebhookByID(long id) {
        return getSnowflakesByID(id)
                .map(SnowflakeSelector::asWebhook);
    }

    default Processor<Role> getRoleByID(long id) {
        return getSnowflakesByID(id).map(SnowflakeSelector::asRole);
    }

    default Processor<User> getUserByID(long id) {
        return getSnowflakesByID(id).map(SnowflakeSelector::asUser);
    }

    default Processor<Channel> getChannelByID(long id) {
        return getSnowflakesByID(id).map(SnowflakeSelector::asChannel);
    }

    default Processor<TextChannel> getTextChannelByID(long id) {
        return getChannelByID(id).map(channel -> channel.asTextChannel().orElse(null));
    }

    default Processor<VoiceChannel> getVoiceChannelByID(long id) {
        return getChannelByID(id).map(channel -> channel.asVoiceChannel().orElse(null));
    }

    default Processor<GuildChannel> getGuildChannelByID(long id) {
        return getChannelByID(id).map(channel -> channel.asGuildChannel().orElse(null));
    }

    default Processor<ChannelCategory> getChannelCategoryByID(long id) {
        return getChannelByID(id).map(channel -> channel.asChannelCategory().orElse(null));
    }

    default Processor<GuildTextChannel> getGuildTextChannelByID(long id) {
        return getChannelByID(id).map(channel -> channel.asGuildTextChannel().orElse(null));
    }

    default Processor<GuildVoiceChannel> getGuildVoiceChannelByID(long id) {
        return getChannelByID(id).map(channel -> channel.asGuildVoiceChannel().orElse(null));
    }

    default Processor<PrivateChannel> getPrivateChannelByID(long id) {
        return getChannelByID(id).map(channel -> channel.asPrivateChannel().orElse(null));
    }

    default Processor<PrivateTextChannel> getPrivateTextChannelByID(long id) {
        return getChannelByID(id).map(channel -> channel.asPrivateTextChannel().orElse(null));
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
        private static final class ShardingManager implements DiscordBot {
            private final String token;
            private final ThreadPool threadPool;
            private final SnowflakeCache entityCache;
            private final REST<DiscordBot> restClient;
            private final List<Shard> shards;
            private final List<WebSocket> webSockets;
            private final int intent;

            @Override
            public String getToken() {
                return token;
            }

            @Override
            public ThreadPool getThreadPool() {
                return threadPool;
            }


            @Override
            public SnowflakeCache getCache() {
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

            private ShardingManager(String token, int intent, int shards, ThreadGroup threadGroup, URI gatewayUri) {
                this.token = token;
                this.intent = intent;
                this.threadPool = ThreadPool.fixedSize(threadGroup, 8 * shards);
                this.entityCache = new SnowflakeCache(this, bind);
                this.restClient = new REST<>(HTTP_ADAPTER, SERIALIZATION_ADAPTER, this);

                final REST.Header.List socketHeaders = new REST.Header.List();
                socketHeaders.add("Authorization", token);
                socketHeaders.add("Content-Type", SERIALIZATION_ADAPTER.getMimeType());

                this.webSockets = Collections.unmodifiableList(IntStream.range(0, shards)
                        .mapToObj(shardId -> HTTP_ADAPTER.createWebSocket(
                                SERIALIZATION_ADAPTER,
                                threadPool,
                                gatewayUri,
                                socketHeaders))
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList()));
                this.shards = Collections.unmodifiableList(IntStream.range(0, shards)
                        .mapToObj(id -> new ShardImpl(this, id, webSockets.get(id)))
                        .collect(Collectors.toList()));
            }
        }

        private static final class ShardImpl implements Shard {
            private final ShardingManager shardingManager;
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
                return shardingManager;
            }

            public ShardImpl(
                    ShardingManager shardingManager, int shardId, WebSocket webSocket
            ) {
                this.shardingManager = shardingManager;
                this.shardId = shardId;
                this.webSocket = webSocket;
            }
        }
    }
}

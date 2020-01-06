package org.comroid.crystalshard.api;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

import org.comroid.crystalshard.adapter.Adapter;
import org.comroid.crystalshard.adapter.CoreAdapter;
import org.comroid.crystalshard.adapter.ImplAdapter;
import org.comroid.crystalshard.api.entity.channel.Channel;
import org.comroid.crystalshard.api.entity.channel.PrivateTextChannel;
import org.comroid.crystalshard.api.entity.guild.Guild;
import org.comroid.crystalshard.api.entity.user.User;
import org.comroid.crystalshard.api.event.EventHandler;
import org.comroid.crystalshard.api.event.multipart.APIEvent;
import org.comroid.crystalshard.api.model.user.Yourself;
import org.comroid.crystalshard.core.cache.CacheManager;
import org.comroid.crystalshard.core.concurrent.ThreadPool;
import org.comroid.crystalshard.core.gateway.Gateway;
import org.comroid.crystalshard.core.rest.DiscordEndpoint;
import org.comroid.crystalshard.core.rest.Ratelimiter;
import org.comroid.crystalshard.core.rest.RestMethod;
import org.comroid.crystalshard.util.annotation.IntroducedBy;

import com.google.common.flogger.FluentLogger;

import static org.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;
import static org.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.PRODUCTION;

public interface Discord extends EventHandler<APIEvent> {
    String getToken();

    int getShardID();

    ThreadPool getCommonThreadPool();

    ThreadPool getGatewayThreadPool();

    ThreadPool getListenerThreadPool();

    ThreadPool getRatelimiterThreadPool();

    Ratelimiter getRatelimiter();

    Gateway getGateway();

    CacheManager getCacheManager();

    Yourself getYourself();

    @IntroducedBy(PRODUCTION)
    default CompletableFuture<Collection<Guild>> requestYourGuilds() {
        return getYourself().requestGuilds();
    }

    @IntroducedBy(PRODUCTION)
    default CompletableFuture<Collection<PrivateTextChannel>> requestYourPrivateMessageChannels() {
        return getYourself().requestPrivateMessageChannels();
    }

    @IntroducedBy(PRODUCTION)
    default CompletableFuture<Collection<User.Connection>> requestYourConnections() {
        return getYourself().requestConnections();
    }

    @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/guild#get-guild")
    default CompletableFuture<Guild> requestGuild(long id) {
        return requestGuild(ThreadPool.getContextApi(), id);
    }

    @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/guild#get-guild")
    default CompletableFuture<Guild> requestGuild(Discord api, long id) {
        return Adapter.<Guild>request(this)
                .endpoint(DiscordEndpoint.GUILD_SPECIFIC, id)
                .method(RestMethod.GET)
                .executeAsObject(data -> Adapter.require(Guild.class, api, data));
    }

    default Optional<? extends Channel> getChannelByID(long id) {
        return getCacheManager()
                .getChannelByID(id);
    }

    final class Builder {
        private final static FluentLogger log = FluentLogger.forEnclosingClass();

        private String token;
        private int shardId = 0;

        static {
            // Initialize Default Adapters
            CoreAdapter.load();
            ImplAdapter.load();

            log.at(Level.INFO).log("Initialized default Adapters");
        }

        public Builder() {
        }

        public int getShardId() {
            return shardId;
        }

        public Builder setShardId(int shardId) {
            this.shardId = shardId;

            return this;
        }

        public Optional<String> getToken() {
            return Optional.ofNullable(token);
        }

        public Builder setToken(String token) {
            this.token = token;

            return this;
        }

        public CompletableFuture<Discord> build() {
            Objects.requireNonNull(token, "Token cannot be null!");
            
            return Adapter.require(Discord.class, token, shardId /* todo: proper shard support */);
        }
    }
}

package de.comroid.crystalshard.api;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

import de.comroid.crystalshard.adapter.Adapter;
import de.comroid.crystalshard.adapter.CoreAdapter;
import de.comroid.crystalshard.adapter.ImplAdapter;
import de.comroid.crystalshard.api.entity.channel.Channel;
import de.comroid.crystalshard.api.entity.channel.PrivateTextChannel;
import de.comroid.crystalshard.api.entity.guild.Guild;
import de.comroid.crystalshard.api.entity.user.User;
import de.comroid.crystalshard.api.event.EventHandler;
import de.comroid.crystalshard.api.event.multipart.APIEvent;
import de.comroid.crystalshard.api.model.user.Yourself;
import de.comroid.crystalshard.core.cache.CacheManager;
import de.comroid.crystalshard.core.concurrent.ThreadPool;
import de.comroid.crystalshard.core.gateway.Gateway;
import de.comroid.crystalshard.core.rest.DiscordEndpoint;
import de.comroid.crystalshard.core.rest.Ratelimiter;
import de.comroid.crystalshard.core.rest.RestMethod;
import de.comroid.crystalshard.util.annotation.IntroducedBy;

import com.google.common.flogger.FluentLogger;

import static de.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;
import static de.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.PRODUCTION;

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

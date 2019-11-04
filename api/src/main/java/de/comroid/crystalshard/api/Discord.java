package de.comroid.crystalshard.api;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import de.comroid.crystalshard.adapter.Adapter;
import de.comroid.crystalshard.api.entity.channel.PrivateTextChannel;
import de.comroid.crystalshard.api.entity.guild.Guild;
import de.comroid.crystalshard.api.entity.user.User;
import de.comroid.crystalshard.api.event.DiscordEvent;
import de.comroid.crystalshard.api.listener.ListenerSpec;
import de.comroid.crystalshard.api.listener.model.ListenerAttachable;
import de.comroid.crystalshard.api.model.user.Yourself;
import de.comroid.crystalshard.core.api.cache.CacheManager;
import de.comroid.crystalshard.core.api.concurrent.ThreadPool;
import de.comroid.crystalshard.core.api.gateway.Gateway;
import de.comroid.crystalshard.core.api.rest.DiscordEndpoint;
import de.comroid.crystalshard.core.api.rest.Ratelimiter;
import de.comroid.crystalshard.core.api.rest.RestMethod;
import de.comroid.crystalshard.util.annotation.IntroducedBy;

import static de.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;
import static de.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.PRODUCTION;

public interface Discord extends ListenerAttachable<ListenerSpec.AttachableTo.Discord<? extends DiscordEvent>> {
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
        return Adapter.<Guild>request(this)
                .endpoint(DiscordEndpoint.GUILD_SPECIFIC, id)
                .method(RestMethod.GET)
                .executeAs(data -> getCacheManager()
                        .updateOrCreateAndGet(Guild.class, id, data));
    }

    static Builder builder() {
        return Adapter.require(Builder.class);
    }

    interface Builder {
        Optional<String> getToken();

        Builder setToken(String token);

        CompletableFuture<Discord> build();
    }
}

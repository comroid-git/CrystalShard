package de.kaleidox.crystalshard.api;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import de.kaleidox.crystalshard.adapter.Adapter;
import de.kaleidox.crystalshard.api.entity.channel.PrivateTextChannel;
import de.kaleidox.crystalshard.api.entity.guild.Guild;
import de.kaleidox.crystalshard.api.entity.user.User;
import de.kaleidox.crystalshard.api.event.DiscordEvent;
import de.kaleidox.crystalshard.api.listener.DiscordAttachableListener;
import de.kaleidox.crystalshard.api.listener.model.ListenerAttachable;
import de.kaleidox.crystalshard.api.model.user.Yourself;
import de.kaleidox.crystalshard.core.api.cache.CacheManager;
import de.kaleidox.crystalshard.core.api.concurrent.ThreadPool;
import de.kaleidox.crystalshard.core.api.gateway.Gateway;
import de.kaleidox.crystalshard.core.api.rest.DiscordEndpoint;
import de.kaleidox.crystalshard.core.api.rest.Ratelimiter;
import de.kaleidox.crystalshard.core.api.rest.RestMethod;
import de.kaleidox.crystalshard.util.annotation.IntroducedBy;

import static de.kaleidox.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;
import static de.kaleidox.crystalshard.util.annotation.IntroducedBy.ImplementationSource.PRODUCTION;

public interface Discord extends ListenerAttachable<DiscordAttachableListener<? extends DiscordEvent>> {
    ThreadPool getCommonThreadPool();

    ThreadPool getGatewayThreadPool();

    ThreadPool getListenerThreadPool();

    ThreadPool getRatelimiterThreadPool();

    Ratelimiter getRatelimiter();

    Gateway getGateway();

    CacheManager getCacheManager();

    Yourself getYourself();

    String getToken();

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
        return Adapter.create(Builder.class);
    }

    interface Builder {
        Optional<String> getToken();

        Builder setToken(String token);

        CompletableFuture<Discord> build();
    }
}

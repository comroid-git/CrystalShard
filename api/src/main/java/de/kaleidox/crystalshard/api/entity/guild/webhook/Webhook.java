package de.kaleidox.crystalshard.api.entity.guild.webhook;

import java.net.URL;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.concurrent.CompletableFuture;

import de.kaleidox.crystalshard.adapter.Adapter;
import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.api.entity.Snowflake;
import de.kaleidox.crystalshard.api.entity.channel.GuildChannel;
import de.kaleidox.crystalshard.api.entity.channel.GuildTextChannel;
import de.kaleidox.crystalshard.api.entity.guild.Guild;
import de.kaleidox.crystalshard.api.entity.user.User;
import de.kaleidox.crystalshard.api.event.guild.webhook.WebhookEvent;
import de.kaleidox.crystalshard.api.listener.guild.WebhookAttachableListener;
import de.kaleidox.crystalshard.api.listener.model.ListenerAttachable;
import de.kaleidox.crystalshard.api.model.message.MessageAuthor;
import de.kaleidox.crystalshard.core.api.cache.Cacheable;
import de.kaleidox.crystalshard.core.api.rest.DiscordEndpoint;
import de.kaleidox.crystalshard.core.api.rest.HTTPStatusCodes;
import de.kaleidox.crystalshard.core.api.rest.RestMethod;
import de.kaleidox.crystalshard.util.annotation.IntroducedBy;

import static de.kaleidox.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;
import static de.kaleidox.crystalshard.util.annotation.IntroducedBy.ImplementationSource.GETTER;
import static de.kaleidox.crystalshard.util.annotation.IntroducedBy.ImplementationSource.PRODUCTION;

public interface Webhook extends MessageAuthor, Snowflake, Cacheable, ListenerAttachable<WebhookAttachableListener<? extends WebhookEvent>> {
    @IntroducedBy(GETTER)
    Optional<Guild> getGuild();

    @IntroducedBy(GETTER)
    GuildTextChannel getChannel();

    @IntroducedBy(GETTER)
    Optional<User> getCreator();

    @IntroducedBy(GETTER)
    Optional<String> getDefaultName();

    @IntroducedBy(GETTER)
    Optional<URL> getAvatarURL();

    @IntroducedBy(GETTER)
    String getToken();

    // missing implementations: https://discordapp.com/developers/docs/resources/webhook#execute-webhook

    @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/webhook#delete-webhook")
    default CompletableFuture<Void> delete() {
        return Adapter.<Void>request(getAPI())
                .endpoint(DiscordEndpoint.WEBHOOK, getID())
                .method(RestMethod.DELETE)
                .expectCode(HTTPStatusCodes.NO_CONTENT)
                .executeAs(data -> getAPI().getCacheManager()
                        .delete(Webhook.class, getID()));
    }

    @Override
    // todo test this behavior
    default OptionalLong getCacheParentID() {
        return getGuild().map(Snowflake::getID)
                .map(OptionalLong::of)
                .orElseGet(OptionalLong::empty);
    }

    @Override
    default Optional<Class<? extends Cacheable>> getCacheParentType() {
        return Optional.of(GuildChannel.class);
    }

    @Override
    default boolean isSubcacheMember() {
        return true;
    }

    @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/webhook#get-webhook")
    static CompletableFuture<Webhook> requestWebhook(Discord api, long id) {
        return Adapter.<Webhook>request(api)
                .endpoint(DiscordEndpoint.WEBHOOK, id)
                .method(RestMethod.GET)
                .executeAs(data -> api.getCacheManager()
                        .updateOrCreateAndGet(Webhook.class, id, data));
    }

    @IntroducedBy(PRODUCTION)
    static Builder builder(Discord api) {
        return Adapter.create(Builder.class, api);
    }

    @IntroducedBy(PRODUCTION)
    interface Builder {
        Optional<String> getName();

        Builder setName(String name);

        Optional<URL> getAvatarURL();

        Builder setAvatar(URL url);

        Optional<GuildTextChannel> getChannel();

        Builder setChannel(GuildTextChannel channel);

        @IntroducedBy(API)
        CompletableFuture<Webhook> build();
    }

    @IntroducedBy(API)
    interface Updater {
        String getName();

        Builder setName(String name);

        URL getAvatar();

        Builder setAvatar(URL url);

        GuildTextChannel getChannel();

        Builder setChannel(GuildTextChannel channel);

        @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/webhook#modify-webhook")
        CompletableFuture<Webhook> update();
    }
}

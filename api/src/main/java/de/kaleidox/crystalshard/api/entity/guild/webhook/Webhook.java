package de.kaleidox.crystalshard.api.entity.guild.webhook;

import java.net.URL;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import de.kaleidox.crystalshard.adapter.Adapter;
import de.kaleidox.crystalshard.adapter.MainAPI;
import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.api.entity.Snowflake;
import de.kaleidox.crystalshard.api.entity.channel.Channel;
import de.kaleidox.crystalshard.api.entity.channel.GuildTextChannel;
import de.kaleidox.crystalshard.api.entity.guild.Guild;
import de.kaleidox.crystalshard.api.entity.user.User;
import de.kaleidox.crystalshard.api.event.guild.webhook.WebhookEvent;
import de.kaleidox.crystalshard.api.listener.guild.WebhookAttachableListener;
import de.kaleidox.crystalshard.api.listener.model.ListenerAttachable;
import de.kaleidox.crystalshard.api.model.message.MessageAuthor;
import de.kaleidox.crystalshard.core.api.cache.CacheManager;
import de.kaleidox.crystalshard.core.api.cache.Cacheable;
import de.kaleidox.crystalshard.core.api.rest.DiscordEndpoint;
import de.kaleidox.crystalshard.core.api.rest.HTTPStatusCodes;
import de.kaleidox.crystalshard.core.api.rest.RestMethod;
import de.kaleidox.crystalshard.util.annotation.IntroducedBy;
import de.kaleidox.crystalshard.util.model.FileType;
import de.kaleidox.crystalshard.util.model.ImageHelper;
import de.kaleidox.crystalshard.util.model.serialization.JsonTrait;
import de.kaleidox.crystalshard.util.model.serialization.JsonTraits;

import com.fasterxml.jackson.databind.JsonNode;

import static de.kaleidox.crystalshard.core.api.cache.Cacheable.makeSubcacheableInfo;
import static de.kaleidox.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;
import static de.kaleidox.crystalshard.util.annotation.IntroducedBy.ImplementationSource.GETTER;
import static de.kaleidox.crystalshard.util.annotation.IntroducedBy.ImplementationSource.PRODUCTION;
import static de.kaleidox.crystalshard.util.model.serialization.JsonTrait.cache;
import static de.kaleidox.crystalshard.util.model.serialization.JsonTrait.identity;
import static de.kaleidox.crystalshard.util.model.serialization.JsonTrait.underlying;

@MainAPI
@JsonTraits(Webhook.Trait.class)
public interface Webhook extends MessageAuthor, Snowflake, Cacheable, ListenerAttachable<WebhookAttachableListener<? extends WebhookEvent>> {
    @CacheInformation.Marker
    CacheInformation<GuildTextChannel> CACHE_INFORMATION = makeSubcacheableInfo(GuildTextChannel.class, Webhook::getChannel);

    @IntroducedBy(GETTER)
    default Optional<Guild> getGuild() {
        return wrapTraitValue(Trait.GUILD);
    }

    @IntroducedBy(GETTER)
    default GuildTextChannel getChannel() {
        return getTraitValue(Trait.CHANNEL);
    }

    @IntroducedBy(GETTER)
    default Optional<User> getCreator() {
        return wrapTraitValue(Trait.CREATOR);
    }

    @IntroducedBy(GETTER)
    default Optional<String> getDefaultName() {
        return wrapTraitValue(Trait.DEFAULT_NAME);
    }

    @IntroducedBy(GETTER)
    default URL getAvatarURL() {
        return wrapTraitValue(Trait.AVATAR_HASH)
                .map(hash -> ImageHelper.USER_AVATAR.url(FileType.PNG, getID(), hash))
                .orElseGet(() -> ImageHelper.DEFAULT_USER_AVATAR.url(FileType.PNG, getID()));
    }

    @IntroducedBy(GETTER)
    default String getToken() {
        return getTraitValue(Trait.TOKEN);
    }

    @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/webhook#delete-webhook")
    default CompletableFuture<Void> delete() {
        return Adapter.<Void>request(getAPI())
                .endpoint(DiscordEndpoint.WEBHOOK, getID())
                .method(RestMethod.DELETE)
                .expectCode(HTTPStatusCodes.NO_CONTENT)
                .executeAs(data -> getAPI().getCacheManager()
                        .delete(Webhook.class, getID()));
    }

    // missing implementations: https://discordapp.com/developers/docs/resources/webhook#execute-webhook

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

    interface Trait {
        JsonTrait<Long, Guild> GUILD = cache("guild_id", CacheManager::getGuildByID);
        JsonTrait<Long, GuildTextChannel> CHANNEL = cache("channel_id", (cache, id) -> cache.getChannelByID(id).flatMap(Channel::asGuildTextChannel));
        JsonTrait<JsonNode, User> CREATOR = underlying("user", User.class);
        JsonTrait<String, String> DEFAULT_NAME = identity(JsonNode::asText, "name");
        JsonTrait<String, String> AVATAR_HASH = identity(JsonNode::asText, "avatar");
        JsonTrait<String, String> TOKEN = identity(JsonNode::asText, "token");
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

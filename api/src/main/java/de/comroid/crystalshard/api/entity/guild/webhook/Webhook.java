package de.comroid.crystalshard.api.entity.guild.webhook;

import java.net.URL;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import de.comroid.crystalshard.adapter.Adapter;
import de.comroid.crystalshard.adapter.MainAPI;
import de.comroid.crystalshard.api.Discord;
import de.comroid.crystalshard.api.entity.Snowflake;
import de.comroid.crystalshard.api.entity.channel.Channel;
import de.comroid.crystalshard.api.entity.channel.GuildTextChannel;
import de.comroid.crystalshard.api.entity.guild.Guild;
import de.comroid.crystalshard.api.entity.user.User;
import de.comroid.crystalshard.api.event.guild.webhook.WebhookEvent;
import de.comroid.crystalshard.api.listener.guild.WebhookAttachableListener;
import de.comroid.crystalshard.api.listener.model.ListenerAttachable;
import de.comroid.crystalshard.api.model.message.MessageAuthor;
import de.comroid.crystalshard.core.api.cache.CacheManager;
import de.comroid.crystalshard.core.api.cache.Cacheable;
import de.comroid.crystalshard.core.api.rest.DiscordEndpoint;
import de.comroid.crystalshard.core.api.rest.HTTPStatusCodes;
import de.comroid.crystalshard.core.api.rest.RestMethod;
import de.comroid.crystalshard.util.annotation.IntroducedBy;
import de.comroid.crystalshard.util.model.FileType;
import de.comroid.crystalshard.util.model.ImageHelper;
import de.comroid.crystalshard.util.model.serialization.JsonBinding;
import de.comroid.crystalshard.util.model.serialization.JsonTraits;

import com.alibaba.fastjson.JSONObject;

import static de.comroid.crystalshard.core.api.cache.Cacheable.makeSubcacheableInfo;
import static de.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;
import static de.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.GETTER;
import static de.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.PRODUCTION;
import static de.comroid.crystalshard.util.model.serialization.JsonBinding.cache;
import static de.comroid.crystalshard.util.model.serialization.JsonBinding.identity;
import static de.comroid.crystalshard.util.model.serialization.JsonBinding.serialize;

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
        return Adapter.require(Builder.class, api);
    }

    interface Trait {
        JsonBinding.TwoStage<Long, Guild> GUILD = cache("guild_id", CacheManager::getGuildByID);
        JsonBinding.TwoStage<Long, GuildTextChannel> CHANNEL = cache("channel_id", (cache, id) -> cache.getChannelByID(id).flatMap(Channel::asGuildTextChannel));
        JsonBinding.TwoStage<JSONObject, User> CREATOR = serialize("user", User.class);
        JsonBinding.OneStage<String> DEFAULT_NAME = identity("name", JSONObject::getString);
        JsonBinding.OneStage<String> AVATAR_HASH = identity("avatar", JSONObject::getString);
        JsonBinding.OneStage<String> TOKEN = identity("token", JSONObject::getString);
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

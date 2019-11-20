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
import de.comroid.crystalshard.api.event.EventHandler;
import de.comroid.crystalshard.api.event.multipart.APIEvent;
import de.comroid.crystalshard.api.model.message.MessageAuthor;
import de.comroid.crystalshard.core.cache.CacheManager;
import de.comroid.crystalshard.core.cache.Cacheable;
import de.comroid.crystalshard.core.rest.DiscordEndpoint;
import de.comroid.crystalshard.core.rest.HTTPStatusCodes;
import de.comroid.crystalshard.core.rest.RestMethod;
import de.comroid.crystalshard.util.annotation.IntroducedBy;
import de.comroid.crystalshard.util.model.FileType;
import de.comroid.crystalshard.util.model.ImageHelper;
import de.comroid.crystalshard.util.model.serialization.JSONBinding;
import de.comroid.crystalshard.util.model.serialization.JSONBindingLocation;

import com.alibaba.fastjson.JSONObject;

import static de.comroid.crystalshard.core.cache.Cacheable.makeSubcacheableInfo;
import static de.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;
import static de.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.GETTER;
import static de.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.PRODUCTION;
import static de.comroid.crystalshard.util.model.serialization.JSONBinding.cache;
import static de.comroid.crystalshard.util.model.serialization.JSONBinding.identity;
import static de.comroid.crystalshard.util.model.serialization.JSONBinding.require;

@MainAPI
@JSONBindingLocation(Webhook.JSON.class)
public interface Webhook extends MessageAuthor, Snowflake, Cacheable, EventHandler<APIEvent> {
    @CacheInformation.Marker
    CacheInformation<GuildTextChannel> CACHE_INFORMATION = makeSubcacheableInfo(GuildTextChannel.class, Webhook::getChannel);

    @IntroducedBy(GETTER)
    default Optional<Guild> getGuild() {
        return wrapBindingValue(JSON.GUILD);
    }

    @IntroducedBy(GETTER)
    default GuildTextChannel getChannel() {
        return getBindingValue(JSON.CHANNEL);
    }

    @IntroducedBy(GETTER)
    default Optional<User> getCreator() {
        return wrapBindingValue(JSON.CREATOR);
    }

    @IntroducedBy(GETTER)
    default Optional<String> getDefaultName() {
        return wrapBindingValue(JSON.DEFAULT_NAME);
    }

    @IntroducedBy(GETTER)
    default URL getAvatarURL() {
        return wrapBindingValue(JSON.AVATAR_HASH)
                .map(hash -> ImageHelper.USER_AVATAR.url(FileType.PNG, getID(), hash))
                .orElseGet(() -> ImageHelper.DEFAULT_USER_AVATAR.url(FileType.PNG, getID()));
    }

    @IntroducedBy(GETTER)
    default String getToken() {
        return getBindingValue(JSON.TOKEN);
    }

    @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/webhook#delete-webhook")
    default CompletableFuture<Void> delete() {
        return Adapter.<Void>request(getAPI())
                .endpoint(DiscordEndpoint.WEBHOOK, getID())
                .method(RestMethod.DELETE)
                .expectCode(HTTPStatusCodes.NO_CONTENT)
                .executeAsObject(data -> getAPI().getCacheManager()
                        .delete(Webhook.class, getID()));
    }

    // missing implementations: https://discordapp.com/developers/docs/resources/webhook#execute-webhook

    @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/webhook#get-webhook")
    static CompletableFuture<Webhook> requestWebhook(Discord api, long id) {
        return Adapter.<Webhook>request(api)
                .endpoint(DiscordEndpoint.WEBHOOK, id)
                .method(RestMethod.GET)
                .executeAsObject(data -> Adapter.require(Webhook.class, api, data));
    }

    @IntroducedBy(PRODUCTION)
    static Builder builder(Discord api) {
        return Adapter.require(Builder.class, api);
    }

    interface JSON {
        JSONBinding.TwoStage<Long, Guild> GUILD = cache("guild_id", CacheManager::getGuildByID);
        JSONBinding.TwoStage<Long, GuildTextChannel> CHANNEL = cache("channel_id", (cache, id) -> cache.getChannelByID(id).flatMap(Channel::asGuildTextChannel));
        JSONBinding.TwoStage<JSONObject, User> CREATOR = require("user", User.class);
        JSONBinding.OneStage<String> DEFAULT_NAME = identity("name", JSONObject::getString);
        JSONBinding.OneStage<String> AVATAR_HASH = identity("avatar", JSONObject::getString);
        JSONBinding.OneStage<String> TOKEN = identity("token", JSONObject::getString);
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

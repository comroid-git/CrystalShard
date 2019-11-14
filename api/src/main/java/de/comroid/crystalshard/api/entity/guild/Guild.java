package de.comroid.crystalshard.api.entity.guild;

import java.awt.Color;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Predicate;
import javax.swing.plaf.synth.Region;

import de.comroid.crystalshard.adapter.Adapter;
import de.comroid.crystalshard.api.Discord;
import de.comroid.crystalshard.api.entity.Snowflake;
import de.comroid.crystalshard.api.entity.channel.Channel;
import de.comroid.crystalshard.api.entity.channel.GuildChannel;
import de.comroid.crystalshard.api.entity.channel.GuildTextChannel;
import de.comroid.crystalshard.api.entity.channel.GuildVoiceChannel;
import de.comroid.crystalshard.api.entity.emoji.CustomEmoji;
import de.comroid.crystalshard.api.entity.guild.webhook.Webhook;
import de.comroid.crystalshard.api.entity.user.GuildMember;
import de.comroid.crystalshard.api.entity.user.User;
import de.comroid.crystalshard.api.listener.ListenerSpec;
import de.comroid.crystalshard.api.listener.model.ListenerAttachable;
import de.comroid.crystalshard.api.model.guild.ban.Ban;
import de.comroid.crystalshard.api.model.guild.invite.Invite;
import de.comroid.crystalshard.api.model.user.Presence;
import de.comroid.crystalshard.api.model.user.Yourself;
import de.comroid.crystalshard.api.model.voice.VoiceRegion;
import de.comroid.crystalshard.api.model.voice.VoiceState;
import de.comroid.crystalshard.core.cache.CacheManager;
import de.comroid.crystalshard.core.cache.Cacheable;
import de.comroid.crystalshard.core.rest.DiscordEndpoint;
import de.comroid.crystalshard.core.rest.HTTPStatusCodes;
import de.comroid.crystalshard.core.rest.RestMethod;
import de.comroid.crystalshard.util.Util;
import de.comroid.crystalshard.util.annotation.IntroducedBy;
import de.comroid.crystalshard.util.model.FileType;
import de.comroid.crystalshard.util.model.ImageHelper;
import de.comroid.crystalshard.util.model.serialization.JsonDeserializable;
import de.comroid.crystalshard.util.model.serialization.JSONBinding;
import de.comroid.crystalshard.util.model.serialization.JSONBindingLocation;

import com.alibaba.fastjson.JSONObject;
import org.jetbrains.annotations.Nullable;

import static de.comroid.crystalshard.core.cache.Cacheable.makeSingletonCacheableInfo;
import static de.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;
import static de.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.GETTER;
import static de.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.PRODUCTION;
import static de.comroid.crystalshard.util.model.serialization.JSONBinding.api;
import static de.comroid.crystalshard.util.model.serialization.JSONBinding.cache;
import static de.comroid.crystalshard.util.model.serialization.JSONBinding.identity;
import static de.comroid.crystalshard.util.model.serialization.JSONBinding.require;
import static de.comroid.crystalshard.util.model.serialization.JSONBinding.simple;
import static de.comroid.crystalshard.util.model.serialization.JSONBinding.mappingCollection;
import static de.comroid.crystalshard.util.model.serialization.JSONBinding.serializableCollection;

@JSONBindingLocation(Guild.JSON.class)
public interface Guild extends Snowflake, ListenerAttachable<ListenerSpec.AttachableTo.Guild>, Cacheable {
    @IntroducedBy(API)
    CompletableFuture<Collection<Webhook>> requestWebhooks();

    @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/emoji#list-guild-emojis")
    CompletableFuture<Collection<CustomEmoji>> requestEmojis();

    @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/emoji#get-guild-emoji")
    default CompletableFuture<CustomEmoji> requestEmoji(long id) {
        return Adapter.<CustomEmoji>request(getAPI())
                .endpoint(DiscordEndpoint.CUSTOM_EMOJI_SPECIFIC, getID(), id)
                .method(RestMethod.GET)
                .executeAsObject(data -> Adapter.require(CustomEmoji.class, getAPI(), data));
    }

    @IntroducedBy(GETTER)
    default String getName() {
        return getBindingValue(JSON.NAME);
    }

    @IntroducedBy(GETTER)
    default Optional<URL> getIconURL() {
        return wrapBindingValue(JSON.ICON_HASH)
                .map(hash -> ImageHelper.GUILD_ICON.url(FileType.PNG, getID(), hash));
    }

    @IntroducedBy(GETTER)
    default Optional<URL> getSplashURL() {
        return wrapBindingValue(JSON.SPLASH_HASH)
                .map(hash -> ImageHelper.GUILD_SPLASH.url(FileType.PNG, getID(), hash));
    }

    @IntroducedBy(GETTER)
    default Optional<GuildMember> getOwner() {
        return wrapBindingValue(JSON.OWNER)
                .flatMap(this::getMember);
    }

    @IntroducedBy(GETTER)
    default Region getRegion() {
        return getBindingValue(JSON.REGION);
    }

    @IntroducedBy(GETTER)
    default Optional<GuildVoiceChannel> getAFKChannel() {
        return wrapBindingValue(JSON.AFK_CHANNEL);
    }

    @IntroducedBy(GETTER)
    default Duration getAFKTimeout() {
        return getBindingValue(JSON.AFK_TIMEOUT);
    }

    @IntroducedBy(GETTER)
    default boolean isEmbeddable() {
        return getBindingValue(JSON.EMBEDDABLE);
    }

    @IntroducedBy(GETTER)
    default Optional<GuildChannel> getEmbedChannel() {
        return wrapBindingValue(JSON.EMBED_CHANNEL);
    }

    @IntroducedBy(GETTER)
    default VerificationLevel getVerificationLevel() {
        return getBindingValue(JSON.VERIFICATION_LEVEL);
    }

    @IntroducedBy(GETTER)
    default DefaultMessageNotificationLevel getDefaultMessageNotificationLevel() {
        return getBindingValue(JSON.DEFAULT_MESSAGE_NOTIFICATION_LEVEL);
    }

    @IntroducedBy(GETTER)
    default ExplicitContentFilterLevel getExplicitContentFilterLevel() {
        return getBindingValue(JSON.EXPLICIT_CONTENT_FILTER_LEVEL);
    }

    @IntroducedBy(GETTER)
    default Collection<Role> getRoles() {
        return getBindingValue(JSON.ROLES);
    }

    @IntroducedBy(GETTER)
    default Collection<CustomEmoji> getEmojis() {
        return getBindingValue(JSON.EMOJIS);
    }

    @IntroducedBy(GETTER)
    default Collection<Feature> getGuildFeatures() {
        return getBindingValue(JSON.FEATURES);
    }

    @IntroducedBy(GETTER)
    default MFALevel getMFALevel() {
        return getBindingValue(JSON.MFA_LEVEL);
    }

    @IntroducedBy(GETTER)
    default Optional<Snowflake> getOwnerApplicationID() {
        return wrapBindingValue(JSON.OWNER_APPLICATION_ID);
    }

    @IntroducedBy(GETTER)
    default boolean isWidgetable() {
        return getBindingValue(JSON.WIDGETABLE);
    }

    @IntroducedBy(GETTER)
    default Optional<GuildChannel> getWidgetChannel() {
        return wrapBindingValue(JSON.WIDGET_CHANNEL);
    }

    @IntroducedBy(GETTER)
    default Optional<GuildTextChannel> getSystemChannel() {
        return wrapBindingValue(JSON.SYSTEM_CHANNEL);
    }

    @IntroducedBy(GETTER)
    default Optional<Instant> getJoinedAt() {
        return wrapBindingValue(JSON.JOINED_AT);
    }

    @IntroducedBy(GETTER)
    default boolean isConsideredLarge() {
        return getBindingValue(JSON.LARGE);
    }

    @IntroducedBy(GETTER)
    default boolean isUnavailable() {
        return getBindingValue(JSON.UNAVAILABLE);
    }

    @IntroducedBy(GETTER)
    default int getMemberCount() {
        return getBindingValue(JSON.MEMBER_COUNT);
    }

    @IntroducedBy(GETTER)
    default Collection<VoiceState> getCurrentVoiceStates() {
        return getBindingValue(JSON.VOICE_STATES);
    }

    @IntroducedBy(GETTER)
    default Collection<GuildMember> getMembers() {
        return getBindingValue(JSON.MEMBERS);
    }

    @IntroducedBy(GETTER)
    default Collection<GuildChannel> getChannels() {
        return getBindingValue(JSON.CHANNELS);
    }

    @IntroducedBy(GETTER)
    default Collection<Presence> getPresences() {
        return getBindingValue(JSON.PRESENCES);
    }

    @IntroducedBy(GETTER)
    default Optional<Integer> getMaximumPresences() {
        return wrapBindingValue(JSON.MAXIMUM_PRESENCES);
    }

    @IntroducedBy(GETTER)
    default Optional<Integer> getMaximumMembers() {
        return wrapBindingValue(JSON.MAXIMUM_MEMBERS);
    }

    @IntroducedBy(GETTER)
    default Optional<URL> getVanityInviteURL() {
        return wrapBindingValue(JSON.VANITY_INVITE_URL);
    }

    @IntroducedBy(GETTER)
    default Optional<String> getDescription() {
        return wrapBindingValue(JSON.DESCRIPTION);
    }

    @IntroducedBy(GETTER)
    default Optional<URL> getBannerURL() {
        return wrapBindingValue(JSON.BANNER_HASH)
                .map(hash -> ImageHelper.GUILD_BANNER.url(FileType.PNG, getID(), hash));
    }

    @IntroducedBy(GETTER)
    default PremiumTier getPremiumTier() {
        return getBindingValue(JSON.PREMIUM_TIER);
    }

    @IntroducedBy(GETTER)
    default int getPremiumSubscriptionCount() {
        return wrapBindingValue(JSON.PREMIUM_SUB_COUNT)
                .orElse(0);
    }

    @IntroducedBy(GETTER)
    default Optional<Locale> getPreferredLocale() {
        return wrapBindingValue(JSON.PREFERRED_LOCALE);
    }

    default Optional<GuildMember> getMember(User user) {
        return user.asGuildMember(this);
    }

    @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/guild#delete-guild")
    default CompletableFuture<Void> delete() {
        return Adapter.<Void>request(getAPI())
                .endpoint(DiscordEndpoint.GUILD_SPECIFIC)
                .method(RestMethod.DELETE)
                .expectCode(HTTPStatusCodes.NO_CONTENT)
                .executeAsObject(data -> getAPI().getCacheManager().delete(Guild.class, getID()));
    }

    @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/guild#get-guild-channels")
    CompletableFuture<Collection<GuildChannel>> requestGuildChannels();

    @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/guild#get-guild-member")
    default CompletableFuture<GuildMember> requestGuildMember(User user) {
        return Adapter.<GuildMember>request(getAPI())
                .endpoint(DiscordEndpoint.GUILD_MEMBER, getID(), user.getID())
                .method(RestMethod.GET)
                .executeAsObject(data -> Adapter.require(GuildMember.class, getAPI(), data));
    }

    @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/guild#list-guild-members")
    CompletableFuture<Collection<GuildMember>> requestGuildMembers();

    @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/guild#add-guild-member")
    CompletableFuture<GuildMember> addMember(User user);

    @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/guild#get-guild-bans")
    CompletableFuture<Collection<Ban>> requestBans();

    @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/guild#get-guild-ban")
    default CompletableFuture<Optional<Ban>> requestBan(User user) {
        return Adapter.<Ban>request(getAPI())
                .endpoint(DiscordEndpoint.BAN_SPECIFIC, getID(), user.getID())
                .method(RestMethod.GET)
                .executeAsObject(data -> Adapter.require(Ban.class, getAPI(), data))
                .thenApply(Optional::ofNullable);
    }

    @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/guild#get-guild-prune-count")
    CompletableFuture<Integer> requestPruneCount(int days);

    @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/guild#begin-guild-prune")
    CompletableFuture<Integer> requestPruneWithCount(int days);

    @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/guild#begin-guild-prune")
    CompletableFuture<Void> requestPruneWithoutCount(int days);

    @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/guild#get-guild-voice-regions")
    CompletableFuture<Collection<VoiceRegion>> requestVoiceRegions();

    @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/guild#get-guild-invites")
    CompletableFuture<Collection<Invite>> requestInvites();

    @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/guild#get-guild-integrations")
    CompletableFuture<Collection<Integration>> requestIntegrations();

    @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/guild#get-guild-embed")
    default CompletableFuture<Guild.Embed> requestGuildEmbed() {
        return Adapter.<Embed>request(getAPI())
                .endpoint(DiscordEndpoint.GUILD_EMBED, getID())
                .method(RestMethod.GET)
                .executeAsObject(data -> Adapter.require(Embed.class, getAPI(), data));
    }

    @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/guild#get-guild-vanity-url")
    CompletableFuture<URL> requestVanityInviteURL();

    @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/guild#get-guild-widget-image")
    default URL getWidgetImageURL(WidgetImageStyle style) {
        try {
            return new URL(DiscordEndpoint.GUILD_WIDGET.uri(getID()) + "?style=" + style.value);
        } catch (MalformedURLException e) {
            throw new AssertionError("Unexpected MalformedURLException", e);
        }
    }

    @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/user#leave-guild")
    default CompletableFuture<Void> leave() {
        return Adapter.<Void>request(getAPI())
                .endpoint(DiscordEndpoint.GUILD_SELF, getID())
                .method(RestMethod.DELETE)
                .expectCode(HTTPStatusCodes.NO_CONTENT)
                .executeAsObject(data -> getAPI().getCacheManager()
                        .delete(Guild.class, getID()));
    }

    default Role getEveryoneRole() {
        return getRoles().stream()
                .filter(role -> role.getName().equals("@everyone"))
                .findFirst()
                .orElseThrow(AssertionError::new);
    }

    Optional<Color> getRoleColor(User ofUser); // todo

    static Builder builder(Discord api) {
        return Adapter.require(Builder.class, api);
    }

    interface JSON extends Snowflake.JSON {
        JSONBinding.OneStage<String> NAME = identity("name", JSONObject::getString);
        JSONBinding.OneStage<String> ICON_HASH = identity("icon", JSONObject::getString);
        JSONBinding.OneStage<String> SPLASH_HASH = identity("splash", JSONObject::getString);
        JSONBinding.TwoStage<Long, User> OWNER = cache("owner_id", CacheManager::getUserByID);
        JSONBinding.TwoStage<String, Region> REGION = simple("region", JSONObject::getString, key -> Adapter.require(VoiceRegion.class, key));
        JSONBinding.TwoStage<Long, GuildVoiceChannel> AFK_CHANNEL = cache("afk_channel_id", (cache, id) -> cache.getChannelByID(id).flatMap(Channel::asGuildVoiceChannel));
        JSONBinding.TwoStage<Integer, Duration> AFK_TIMEOUT = simple("afk_timeout", JSONObject::getInteger, Duration::ofSeconds);
        JSONBinding.OneStage<Boolean> EMBEDDABLE = identity("embed_enabled", JSONObject::getBoolean);
        JSONBinding.TwoStage<Long, GuildChannel> EMBED_CHANNEL = cache("embed_channel_id", (cache, id) -> cache.getChannelByID(id).flatMap(Channel::asGuildChannel));
        JSONBinding.TwoStage<Integer, VerificationLevel> VERIFICATION_LEVEL = simple("verification_level", JSONObject::getInteger, VerificationLevel::valueOf);
        JSONBinding.TwoStage<Integer, DefaultMessageNotificationLevel> DEFAULT_MESSAGE_NOTIFICATION_LEVEL = simple("default_message_notifications", JSONObject::getInteger, DefaultMessageNotificationLevel::valueOf);
        JSONBinding.TwoStage<Integer, ExplicitContentFilterLevel> EXPLICIT_CONTENT_FILTER_LEVEL = simple("explicit_content_filter", JSONObject::getInteger, ExplicitContentFilterLevel::valueOf);
        JSONBinding.TriStage<JSONObject, Role> ROLES = serializableCollection("roles", Role.class);
        JSONBinding.TriStage<JSONObject, CustomEmoji> EMOJIS = serializableCollection("emojis", CustomEmoji.class);
        JSONBinding.TriStage<String, Feature> FEATURES = mappingCollection("features", JSONObject::getString, (api, key) -> Feature.valueOf(key));
        JSONBinding.TwoStage<Integer, MFALevel> MFA_LEVEL = simple("mfa_level", JSONObject::getInteger, MFALevel::valueOf);
        JSONBinding.TwoStage<Long, Snowflake> OWNER_APPLICATION_ID = api("application_id", JSONObject::getLong, (api, id) -> Adapter.require(Snowflake.class, api, id));
        JSONBinding.OneStage<Boolean> WIDGETABLE = identity("widget_enabled", JSONObject::getBoolean);
        JSONBinding.TwoStage<Long, GuildChannel> WIDGET_CHANNEL = cache("widget_channel_id", (cache, id) -> cache.getChannelByID(id).flatMap(Channel::asGuildChannel));
        JSONBinding.TwoStage<Long, GuildTextChannel> SYSTEM_CHANNEL = cache("system_channel_id", (cache, id) -> cache.getChannelByID(id).flatMap(Channel::asGuildTextChannel));
        JSONBinding.TwoStage<String, Instant> JOINED_AT = simple("joined_at", JSONObject::getString, Instant::parse);
        JSONBinding.OneStage<Boolean> LARGE = identity("large", JSONObject::getBoolean);
        JSONBinding.OneStage<Boolean> UNAVAILABLE = identity("unavailable", JSONObject::getBoolean);
        JSONBinding.OneStage<Integer> MEMBER_COUNT = identity("member_count", JSONObject::getInteger);
        JSONBinding.TriStage<JSONObject, VoiceState> VOICE_STATES = serializableCollection("voice_states", VoiceState.class);
        JSONBinding.TriStage<JSONObject, GuildMember> MEMBERS = serializableCollection("members", GuildMember.class);
        JSONBinding.TriStage<JSONObject, GuildChannel> CHANNELS = serializableCollection("channels", GuildChannel.class);
        JSONBinding.TriStage<JSONObject, Presence> PRESENCES = serializableCollection("presences", Presence.class);
        JSONBinding.OneStage<Integer> MAXIMUM_PRESENCES = identity("max_presences", JSONObject::getInteger);
        JSONBinding.OneStage<Integer> MAXIMUM_MEMBERS = identity("max_members", JSONObject::getInteger);
        JSONBinding.TwoStage<String, URL> VANITY_INVITE_URL = simple("vanity_url_code", JSONObject::getString, code -> Util.createUrl$rethrow("https://discord.gg/" + code));
        JSONBinding.OneStage<String> DESCRIPTION = identity("description", JSONObject::getString);
        JSONBinding.OneStage<String> BANNER_HASH = identity("banner", JSONObject::getString);
        JSONBinding.TwoStage<Integer, PremiumTier> PREMIUM_TIER = simple("premium_tier", JSONObject::getInteger, PremiumTier::valueOf);
        JSONBinding.OneStage<Integer> PREMIUM_SUB_COUNT = identity("premium_subscription_count", JSONObject::getInteger);
        JSONBinding.TwoStage<String, Locale> PREFERRED_LOCALE = simple("preferred_locale", JSONObject::getString, Locale::forLanguageTag);
    }

    @JSONBindingLocation(Embed.JSON.class)
    interface Embed extends Cacheable, JsonDeserializable {
        @CacheInformation.Marker
        CacheInformation<Guild> CACHE_INFORMATION = makeSingletonCacheableInfo(Guild.class, Embed::getGuild);
        
        Guild getGuild();

        default boolean isEnabled() {
            return getBindingValue(JSON.ENABLED);
        }

        default Optional<GuildChannel> getChannel() {
            return wrapBindingValue(JSON.CHANNEL);
        }

        interface JSON {
            JSONBinding.OneStage<Boolean> ENABLED = identity("enabled", JSONObject::getBoolean);
            JSONBinding.TwoStage<Long, GuildChannel> CHANNEL = cache("channel_id", (cache, id) -> cache.getChannelByID(id).flatMap(Channel::asGuildChannel));
        }

        interface Updater {
            Optional<GuildChannel> getChannel();

            Updater setChannel(GuildChannel channel);

            boolean isEnabled();

            Updater setEnabled(boolean enabled);

            @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/guild#modify-guild-embed")
            CompletableFuture<Embed> update();
        }
    }

    @JSONBindingLocation(Integration.JSON.class)
    interface Integration extends Snowflake, JsonDeserializable, Cacheable {
        @CacheInformation.Marker
        CacheInformation<Guild> CACHE_INFORMATION = makeSingletonCacheableInfo(Guild.class, Integration::getGuild);
        
        Guild getGuild();

        @IntroducedBy(GETTER)
        default String getName() {
            return getBindingValue(JSON.NAME);
        }

        @IntroducedBy(GETTER)
        default String getType() {
            return getBindingValue(JSON.TYPE);
        }

        @IntroducedBy(GETTER)
        default boolean isEnabled() {
            return getBindingValue(JSON.ENABLED);
        }

        @IntroducedBy(GETTER)
        default boolean isSyncing() {
            return getBindingValue(JSON.IS_SYNCING);
        }

        @IntroducedBy(GETTER)
        default Role getSubscriberRole() {
            return getBindingValue(JSON.PREMIUM_ROLE);
        }

        @IntroducedBy(GETTER)
        default int getExpireBehavior() {
            return getBindingValue(JSON.EXPIRE_BEHAVIOR);
        }

        @IntroducedBy(GETTER)
        default int getExpireGracePeriod() {
            return getBindingValue(JSON.EXPIRE_GRACE_PERIOD);
        }

        @IntroducedBy(GETTER)
        default User getUser() {
            return getBindingValue(JSON.USER);
        }

        @IntroducedBy(GETTER)
        default Account getAccount() {
            return getBindingValue(JSON.ACCOUNT);
        }

        @IntroducedBy(GETTER)
        default Instant getSyncedAtTimestamp() {
            return getBindingValue(JSON.SYNCED_AT);
        }

        interface JSON extends Snowflake.JSON {
            JSONBinding.OneStage<String> NAME = identity("name", JSONObject::getString);
            JSONBinding.OneStage<String> TYPE = identity("type", JSONObject::getString);
            JSONBinding.OneStage<Boolean> ENABLED = identity("enabled", JSONObject::getBoolean);
            JSONBinding.OneStage<Boolean> IS_SYNCING = identity("syncing", JSONObject::getBoolean);
            JSONBinding.TwoStage<Long, Role> PREMIUM_ROLE = cache("role_id", CacheManager::getRoleByID);
            JSONBinding.OneStage<Integer> EXPIRE_BEHAVIOR = identity("expire_behavior", JSONObject::getInteger);
            JSONBinding.OneStage<Integer> EXPIRE_GRACE_PERIOD = identity("expire_grace_period", JSONObject::getInteger);
            JSONBinding.TwoStage<JSONObject, User> USER = require("user", User.class);
            JSONBinding.TwoStage<JSONObject, Account> ACCOUNT = require("account", Account.class);
            JSONBinding.TwoStage<String, Instant> SYNCED_AT = simple("synced_at", JSONObject::getString, Instant::parse);
        }

        @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/guild#delete-guild-integration")
        default CompletableFuture<Void> delete() {
            return Adapter.<Void>request(getAPI())
                    .endpoint(DiscordEndpoint.INTEGRATION_SPECIFIC, getGuild().getID(), getID())
                    .method(RestMethod.DELETE)
                    .expectCode(HTTPStatusCodes.NO_CONTENT)
                    .executeAsObject(data -> getAPI().getCacheManager()
                            .deleteMember(Guild.class, Integration.class, getGuild().getID(), getID()));
            // todo add thenCompose waiting for deletion listener?
        }

        @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/guild#sync-guild-integration")
        default CompletableFuture<Void> sync() {
            return Adapter.<Void>request(getAPI())
                    .endpoint(DiscordEndpoint.INTEGRATION_SPECIFIC_SYNC, getGuild().getID(), getID())
                    .method(RestMethod.POST)
                    .expectCode(HTTPStatusCodes.NO_CONTENT)
                    .executeAsObject(data -> null);
        }

        @JSONBindingLocation(Account.JSON.class)
        interface Account extends JsonDeserializable {
            default String getID() {
                return getBindingValue(JSON.ID);
            }

            default String getName() {
                return getBindingValue(JSON.NAME);
            }

            interface JSON {
                JSONBinding.OneStage<String> ID = identity("id", JSONObject::getString);
                JSONBinding.OneStage<String> NAME = identity("name", JSONObject::getString);
            }
        }

        interface Builder { // todo
            @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/guild#create-guild-integration")
            CompletableFuture<Integration> build();
        }

        interface Updater { // todo
            @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/guild#modify-guild-integration")
            CompletableFuture<Integration> update();
        }
    }

    @IntroducedBy(PRODUCTION)
    interface Builder {
        Optional<String> getName();

        Builder setName(String name);

        Optional<VoiceRegion> getVoiceRegion();

        Builder setVoiceRegion(VoiceRegion region);

        Optional<InputStream> getIconInputStream();

        Builder setIcon(InputStream inputStream);

        Builder setIcon(URL url);

        VerificationLevel getVerificationLevel();

        Builder setVerificationLevel(VerificationLevel verificationLevel);

        DefaultMessageNotificationLevel getDefaultMessageNotificationLevel();

        Builder setDefaultMessageNotificationLevel(DefaultMessageNotificationLevel defaultMessageNotificationLevel);

        ExplicitContentFilterLevel getExplicitContentFilterLevel();

        Builder setExplicitContentFilterLevel(ExplicitContentFilterLevel explicitContentFilterLevel);

        Builder modifyEveryoneRole(Consumer<Role.Builder> everyoneRoleModifier);

        Collection<Role.Builder> getAdditionalRoles();

        Builder addAdditionalRole(Role.Builder role);

        Builder removeAdditionalRoleIf(Predicate<Role.Builder> tester);

        Collection<GuildChannel.Builder<? extends GuildChannel, ? extends GuildChannel.Builder>> getChannels();

        Builder addChannel(GuildChannel.Builder<? extends GuildChannel, ? extends GuildChannel.Builder> channel);

        Builder removeChannelIf(Predicate<GuildChannel.Builder<? extends GuildChannel, ? extends GuildChannel.Builder>> tester);

        @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/guild#create-guild")
        CompletableFuture<Guild> build();
    }

    @IntroducedBy(PRODUCTION)
    interface Updater {
        String getName();

        Updater setName(String name);

        VoiceRegion getVoiceRegion();

        Updater setVoiceRegion(VoiceRegion voiceRegion);

        VerificationLevel getVerificationLevel();

        Updater setVerificationLevel(VerificationLevel verificationLevel);

        DefaultMessageNotificationLevel getDefaultMessageNotificationLevel();

        Updater setDefaultMessageNotificationLevel(DefaultMessageNotificationLevel defaultMessageNotificationLevel);

        ExplicitContentFilterLevel getExplicitContentFilterLevel();

        Updater setExplicitContentFilterLevel(ExplicitContentFilterLevel explicitContentFilterLevel);

        Optional<GuildVoiceChannel> getAFKChannel();

        Updater setAFKChannel(GuildVoiceChannel afkChannel);

        Optional<Duration> getAFKTimeout();

        Updater setAFKTimeout(long time, TimeUnit unit);

        InputStream getIconInputStream();

        Updater setIcon(InputStream inputStream);

        Updater setIcon(URL url);

        GuildMember getOwner();

        Updater setOwner(GuildMember owner);

        InputStream getSplashImageInputStream();

        Updater setSplashImage(InputStream inputStream);

        Updater setSplashImage(URL url);

        Optional<GuildTextChannel> getSystemChannel();

        Updater setSystemChannel(GuildTextChannel systemChannel);

        @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/guild#modify-guild")
        CompletableFuture<Guild> update();
    }

    enum WidgetImageStyle {
        /**
         * shield style widget with Discord icon and guild members online count
         * <p>
         * Example: https://discordapp.com/api/guilds/81384788765712384/widget.png?style=shield
         */
        SHIELD("shield"),

        /**
         * large image with guild icon, name and online count.
         * "POWERED BY DISCORD" as the footer of the widget
         * <p>
         * Example: https://discordapp.com/api/guilds/81384788765712384/widget.png?style=banner1
         */
        POWERED_BY_DISCORD("banner1"),

        /**
         * smaller widget style with guild icon, name and online count.
         * Split on the right with Discord logo
         * <p>
         * Example: https://discordapp.com/api/guilds/81384788765712384/widget.png?style=banner2
         */
        SMALL("banner2"),

        /**
         * large image with guild icon, name and online count. In the footer,
         * Discord logo on the left and "Chat Now" on the right
         * <p>
         * Example: https://discordapp.com/api/guilds/81384788765712384/widget.png?style=banner3
         */
        CHAT_NOW("banner3"),

        /**
         * large Discord logo at the top of the widget. Guild icon, name and online count in the middle
         * portion of the widget and a "JOIN MY SERVER" button at the bottom
         * <p>
         * Example: https://discordapp.com/api/guilds/81384788765712384/widget.png?style=banner4
         */
        JOIN_MY_SERVER("banner4");

        public final String value;

        WidgetImageStyle(String value) {
            this.value = value;
        }
    }

    enum DefaultMessageNotificationLevel {
        ALL_MESSAGES(0),

        ONLY_MENTIONS(1);

        public final int value;

        DefaultMessageNotificationLevel(int value) {
            this.value = value;
        }

        public static @Nullable DefaultMessageNotificationLevel valueOf(int value) {
            for (DefaultMessageNotificationLevel level : values())
                if (level.value == value)
                    return level;

            return null;
        }
    }

    enum ExplicitContentFilterLevel {
        DISABLED(0),

        MEMBERS_WITHOUT_ROLES(1),

        ALL_MEMBERS(2);

        public final int value;

        ExplicitContentFilterLevel(int value) {
            this.value = value;
        }

        public static @Nullable ExplicitContentFilterLevel valueOf(int value) {
            for (ExplicitContentFilterLevel level : values())
                if (level.value == value)
                    return level;

            return null;
        }
    }

    enum MFALevel {
        NONE(0),

        ELEVATED(1);

        public final int value;

        MFALevel(int value) {
            this.value = value;
        }

        public static @Nullable MFALevel valueOf(int value) {
            for (MFALevel level : values())
                if (level.value == value)
                    return level;

            return null;
        }
    }

    enum VerificationLevel {
        /**
         * unrestricted
         */
        NONE(0),

        /**
         * must have verified email on account
         */
        LOW(1),

        /**
         * must be registered on Discord for longer than 5 minutes
         */
        MEDIUM(2),

        /**
         * (╯°□°）╯︵ ┻━┻ - must be a member of the server for longer than 10 minutes
         */
        HIGH(3),

        /**
         * ┻━┻ ミヽ(ಠ 益 ಠ)ﾉ彡 ┻━┻ - must have a verified phone number
         */
        VERY_HIGH(4);

        public final int value;

        VerificationLevel(int value) {
            this.value = value;
        }

        public static @Nullable VerificationLevel valueOf(int value) {
            for (VerificationLevel level : values())
                if (level.value == value)
                    return level;

            return null;
        }
    }

    enum PremiumTier {
        NONE(0),
        TIER_1(1),
        TIER_2(2),
        TIER_3(3);

        public final int value;

        PremiumTier(int value) {
            this.value = value;
        }

        public static @Nullable PremiumTier valueOf(int value) {
            for (PremiumTier tier : values())
                if (tier.value == value)
                    return tier;

            return null;
        }
    }

    enum Feature {
        /**
         * guild has access to set an invite splash background
         */
        INVITE_SPLASH,

        /**
         * guild has access to set 320kbps bitrate in voice (previously VIP voice servers)
         */
        VIP_REGIONS,

        /**
         * guild has access to set a vanity URL
         */
        VANITY_URL,

        /**
         * guild is verified
         */
        VERIFIED,

        /**
         * guild is partnered
         */
        PARTNERED,

        /**
         * guild is lurkable
         */
        LURKABLE,

        /**
         * guild has access to use commerce features (i.e. create store channels)
         */
        COMMERCE,

        /**
         * guild has access to create news channels
         */
        NEWS,

        /**
         * guild is able to be discovered in the directory
         */
        DISCOVERABLE,

        /**
         * guild is able to be featured in the directory
         */
        FEATURABLE,

        /**
         * guild has access to set an animated guild icon
         */
        ANIMATED_ICON,

        /**
         * guild has access to set a guild banner image
         */
        BANNER
    }
}

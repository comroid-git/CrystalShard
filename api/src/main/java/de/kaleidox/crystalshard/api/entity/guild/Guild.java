package de.kaleidox.crystalshard.api.entity.guild;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.Locale;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Predicate;
import javax.swing.plaf.synth.Region;

import de.kaleidox.crystalshard.adapter.Adapter;
import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.api.entity.Snowflake;
import de.kaleidox.crystalshard.api.entity.channel.GuildChannel;
import de.kaleidox.crystalshard.api.entity.channel.GuildTextChannel;
import de.kaleidox.crystalshard.api.entity.channel.GuildVoiceChannel;
import de.kaleidox.crystalshard.api.entity.emoji.CustomEmoji;
import de.kaleidox.crystalshard.api.entity.user.GuildMember;
import de.kaleidox.crystalshard.api.entity.user.User;
import de.kaleidox.crystalshard.api.entity.guild.webhook.Webhook;
import de.kaleidox.crystalshard.api.listener.guild.GuildAttachableListener;
import de.kaleidox.crystalshard.api.listener.model.ListenerAttachable;
import de.kaleidox.crystalshard.api.model.guild.ban.Ban;
import de.kaleidox.crystalshard.api.model.guild.invite.Invite;
import de.kaleidox.crystalshard.api.model.user.Presence;
import de.kaleidox.crystalshard.api.model.voice.VoiceRegion;
import de.kaleidox.crystalshard.api.model.voice.VoiceState;
import de.kaleidox.crystalshard.core.api.cache.CacheManager;
import de.kaleidox.crystalshard.core.api.cache.Cacheable;
import de.kaleidox.crystalshard.core.api.rest.DiscordEndpoint;
import de.kaleidox.crystalshard.core.api.rest.HTTPStatusCodes;
import de.kaleidox.crystalshard.core.api.rest.RestMethod;
import de.kaleidox.crystalshard.util.annotation.IntroducedBy;

import static de.kaleidox.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;
import static de.kaleidox.crystalshard.util.annotation.IntroducedBy.ImplementationSource.GETTER;
import static de.kaleidox.crystalshard.util.annotation.IntroducedBy.ImplementationSource.PRODUCTION;

public interface Guild extends Snowflake, ListenerAttachable<GuildAttachableListener>, Cacheable {
    @IntroducedBy(API)
    CompletableFuture<Collection<Webhook>> requestWebhooks();

    @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/emoji#list-guild-emojis")
    CompletableFuture<Collection<CustomEmoji>> requestEmojis();

    @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/emoji#get-guild-emoji")
    default CompletableFuture<CustomEmoji> requestEmoji(long id) {
        return Adapter.<CustomEmoji>request(getAPI())
                .endpoint(DiscordEndpoint.CUSTOM_EMOJI_SPECIFIC, getID(), id)
                .method(RestMethod.GET)
                .executeAs(data -> getAPI().getCacheManager().updateOrCreateAndGet(CustomEmoji.class, id, data));
    }

    @IntroducedBy(GETTER)
    String getName();

    @IntroducedBy(GETTER)
    Optional<URL> getIconURL();

    @IntroducedBy(GETTER)
    Optional<URL> getSplashURL();

    @IntroducedBy(GETTER)
    Optional<GuildMember> getOwner();

    @IntroducedBy(GETTER)
    Region getRegion();

    @IntroducedBy(GETTER)
    Optional<GuildVoiceChannel> getAFKChannel();

    @IntroducedBy(GETTER)
    int getAFKTimeout();

    @IntroducedBy(GETTER)
    boolean isEmbeddable();

    @IntroducedBy(GETTER)
    Optional<GuildChannel> getEmbedChannel();

    @IntroducedBy(GETTER)
    VerificationLevel getVerificationLevel();

    @IntroducedBy(GETTER)
    DefaultMessageNotificationLevel getDefaultMessageNotificationLevel();

    @IntroducedBy(GETTER)
    ExplicitContentFilterLevel getExplicitContentFilterLevel();

    @IntroducedBy(GETTER)
    Collection<Role> getRoles();

    @IntroducedBy(GETTER)
    Collection<CustomEmoji> getEmojis();

    @IntroducedBy(GETTER)
    Collection<Feature> getGuildFeatures();

    @IntroducedBy(GETTER)
    MFALevel getMFALevel();

    @IntroducedBy(GETTER)
    Optional<Snowflake> getOwnerApplicationID();

    @IntroducedBy(GETTER)
    boolean isWidgetable();

    @IntroducedBy(GETTER)
    Optional<GuildChannel> getWidgetChannel();

    @IntroducedBy(GETTER)
    Optional<GuildTextChannel> getSystemChannel();

    @IntroducedBy(GETTER)
    Optional<Instant> getJoinedAt(); // ?

    @IntroducedBy(GETTER)
    boolean isConsideredLarge();

    @IntroducedBy(GETTER)
    boolean isUnavailable();

    @IntroducedBy(GETTER)
    int getMemberCount();

    @IntroducedBy(GETTER)
    Collection<VoiceState> getCurrentVoiceStates();

    @IntroducedBy(GETTER)
    Collection<GuildMember> getMembers();

    @IntroducedBy(GETTER)
    Collection<GuildChannel> getChannels();

    @IntroducedBy(GETTER)
    Collection<Presence> getPresences();

    @IntroducedBy(GETTER)
    OptionalInt getMaximumPresences();

    @IntroducedBy(GETTER)
    OptionalInt getMaximumMembers();

    @IntroducedBy(GETTER)
    Optional<URL> getVanityInviteURL();

    @IntroducedBy(GETTER)
    Optional<String> getDescription();

    @IntroducedBy(GETTER)
    Optional<URL> getBannerURL();

    @IntroducedBy(GETTER)
    PremiumTier getPremiumTier();

    @IntroducedBy(GETTER)
    OptionalInt getPremiumSubscriptionCount();

    @IntroducedBy(GETTER)
    Locale getPreferredLocale();

    @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/guild#delete-guild")
    default CompletableFuture<Void> delete() {
        return Adapter.<Void>request(getAPI())
                .endpoint(DiscordEndpoint.GUILD_SPECIFIC)
                .method(RestMethod.DELETE)
                .expectCode(HTTPStatusCodes.NO_CONTENT)
                .executeAs(data -> getAPI().getCacheManager().delete(Guild.class, getID()));
    }

    @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/guild#get-guild-channels")
    CompletableFuture<Collection<GuildChannel>> requestGuildChannels();

    @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/guild#get-guild-member")
    default CompletableFuture<GuildMember> requestGuildMember(User user) {
        return Adapter.<GuildMember>request(getAPI())
                .endpoint(DiscordEndpoint.GUILD_MEMBER, getID(), user.getID())
                .method(RestMethod.GET)
                .executeAs(data -> getAPI().getCacheManager()
                        .updateOrCreateAndGet(GuildMember.class, user.getID(), data));
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
                .executeAs(data -> getAPI().getCacheManager()
                        .updateOrCreateMemberAndGet(Guild.class, Ban.class, getID(), user.getID(), data))
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
                .executeAs(data -> getAPI().getCacheManager()
                        .updateOrCreateSingletonMemberAndGet(Guild.class, Embed.class, getID(), data));
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
                .executeAs(data -> getAPI().getCacheManager()
                        .delete(Guild.class, getID()));
    }

    static Builder builder(Discord api) {
        return Adapter.create(Builder.class, api);
    }

    interface Embed extends Cacheable {
        Guild getGuild();

        boolean isEnabled();

        Optional<GuildChannel> getChannel();

        @Override
        default OptionalLong getCacheParentID() {
            return OptionalLong.of(getGuild().getID());
        }

        @Override
        default Optional<Class<? extends Cacheable>> getCacheParentType() {
            return Optional.of(Guild.class);
        }

        @Override
        default boolean isSingletonType() {
            return true;
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

    interface Integration extends Snowflake, Cacheable {
        Guild getGuild();

        @IntroducedBy(GETTER)
        String getName();

        @IntroducedBy(GETTER)
        String getType();

        @IntroducedBy(GETTER)
        boolean isEnabled();

        @IntroducedBy(GETTER)
        boolean isSyncing();

        @IntroducedBy(GETTER)
        Role getSubscriberRole();

        @IntroducedBy(GETTER)
        int getExpireBehavior();

        @IntroducedBy(GETTER)
        int getExpireGracePeriod();

        @IntroducedBy(GETTER)
        User getUser();

        @IntroducedBy(GETTER)
        Account getAccount();

        @IntroducedBy(GETTER)
        Instant getSyncedAtTimestamp();

        @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/guild#delete-guild-integration")
        default CompletableFuture<Void> delete() {
            return Adapter.<Void>request(getAPI())
                    .endpoint(DiscordEndpoint.INTEGRATION_SPECIFIC, getGuild().getID(), getID())
                    .method(RestMethod.DELETE)
                    .expectCode(HTTPStatusCodes.NO_CONTENT)
                    .executeAs(data -> getAPI().getCacheManager()
                            .deleteMember(Guild.class, Integration.class, getGuild().getID(), getID()));
            // todo add thenCompose waiting for deletion listener?
        }

        @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/guild#sync-guild-integration")
        default CompletableFuture<Void> sync() {
            return Adapter.<Void>request(getAPI())
                    .endpoint(DiscordEndpoint.INTEGRATION_SPECIFIC_SYNC, getGuild().getID(), getID())
                    .method(RestMethod.POST)
                    .expectCode(HTTPStatusCodes.NO_CONTENT)
                    .executeAs(data -> null);
        }

        @Override
        default OptionalLong getCacheParentID() {
            return OptionalLong.of(getGuild().getID());
        }

        @Override
        default Optional<Class<? extends Cacheable>> getCacheParentType() {
            return Optional.of(Guild.class);
        }

        @Override
        default boolean isSingletonType() {
            return true;
        }

        interface Account {
            String getID();

            String getName();
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
    }

    enum ExplicitContentFilterLevel {
        DISABLED(0),

        MEMBERS_WITHOUT_ROLES(1),

        ALL_MEMBERS(2);

        public final int value;

        ExplicitContentFilterLevel(int value) {
            this.value = value;
        }
    }

    enum MFALevel {
        NONE(0),

        ELEVATED(1);

        public final int value;

        MFALevel(int value) {
            this.value = value;
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

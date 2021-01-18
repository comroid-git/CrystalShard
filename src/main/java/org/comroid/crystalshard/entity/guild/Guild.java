package org.comroid.crystalshard.entity.guild;

import org.comroid.api.ContextualProvider;
import org.comroid.api.Named;
import org.comroid.api.Polyfill;
import org.comroid.crystalshard.entity.SnowflakeCache;
import org.comroid.crystalshard.cdn.CDNEndpoint;
import org.comroid.crystalshard.cdn.ImageType;
import org.comroid.crystalshard.entity.EntityType;
import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.crystalshard.entity.channel.Channel;
import org.comroid.crystalshard.entity.user.User;
import org.comroid.crystalshard.model.guild.*;
import org.comroid.crystalshard.model.voice.VoiceRegion;
import org.comroid.crystalshard.model.voice.VoiceState;
import org.comroid.mutatio.span.Span;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.uniform.node.impl.StandardValueType;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;

import java.net.URL;
import java.time.Instant;
import java.util.Locale;
import java.util.Set;

public final class Guild extends Snowflake.Abstract implements Named {
    @RootBind
    public static final GroupBind<Guild> TYPE = BASETYPE.rootGroup("guild");
    public static final VarBind<Guild, String, String, String> ICON_HASH
            = TYPE.createBind("icon")
            .extractAs(StandardValueType.STRING)
            .build();
    public static final VarBind<Guild, String, String, String> SPLASH_HASH
            = TYPE.createBind("splash")
            .extractAs(StandardValueType.STRING)
            .build();
    public static final VarBind<Guild, String, String, String> DISCOVERY_SPLASH_HASH
            = TYPE.createBind("discovery_splash_hash")
            .extractAs(StandardValueType.STRING)
            .build();
    public static final VarBind<Guild, Boolean, Boolean, Boolean> IS_OWNER
            = TYPE.createBind("owner")
            .extractAs(StandardValueType.BOOLEAN)
            .build();
    public static final VarBind<Guild, Long, User, User> OWNER
            = TYPE.createBind("owner_id")
            .extractAs(StandardValueType.LONG)
            .andResolve((guild, owner) -> guild.requireFromContext(SnowflakeCache.class)
                    .getUser(owner).get())
            .build();
    public static final VarBind<Guild, String, VoiceRegion, VoiceRegion> VOICE_REGION
            = TYPE.createBind("region")
            .extractAs(StandardValueType.STRING)
            .andResolve(VoiceRegion::find)
            .build();
    public static final VarBind<Guild, Long, Channel, Channel> AFK_CHANNEL
            = TYPE.createBind("afk_channel_id")
            .extractAs(StandardValueType.LONG)
            .andResolve((guild, channel) -> guild.requireFromContext(SnowflakeCache.class)
                    .getChannel(channel).get())
            .build();
    public static final VarBind<Guild, Integer, Integer, Integer> AFK_TIMEOUT
            = TYPE.createBind("afk_timeout")
            .extractAs(StandardValueType.INTEGER)
            .build();
    public static final VarBind<Guild, Boolean, Boolean, Boolean> IS_WIDGET_ENABLED
            = TYPE.createBind("widget_enabled")
            .extractAs(StandardValueType.BOOLEAN)
            .build();
    public static final VarBind<Guild, Long, Channel, Channel> WIDGET_CHANNEL_ID
            = TYPE.createBind("widget_channel_id")
            .extractAs(StandardValueType.LONG)
            .andResolve((guild, channel) -> guild.requireFromContext(SnowflakeCache.class)
                    .getChannel(channel).get())
            .build();
    public static final VarBind<Guild, Integer, VerificationLevel, VerificationLevel> VERIFICATION_LEVEL
            = TYPE.createBind("verification_level")
            .extractAs(StandardValueType.INTEGER)
            .andRemapRef(VerificationLevel::valueOf)
            .build();
    public static final VarBind<Guild, Integer, DefaultMessageNotificationLevel, DefaultMessageNotificationLevel> DEFAULT_NOTIFICATION_LEVEL
            = TYPE.createBind("default_message_notifications")
            .extractAs(StandardValueType.INTEGER)
            .andRemapRef(DefaultMessageNotificationLevel::valueOf)
            .build();
    public static final VarBind<Guild, Integer, ExplicitContentFilter, ExplicitContentFilter> EXPLICIT_CONTENT_FILTER_LEVEL
            = TYPE.createBind("explicit_content_filter")
            .extractAs(StandardValueType.INTEGER)
            .andRemapRef(ExplicitContentFilter::valueOf)
            .build();
    public static final VarBind<Guild, UniObjectNode, Role, Span<Role>> ROLES
            = TYPE.createBind("roles")
            .extractAsArray()
            .andResolve(Role::resolve)
            .intoSpan()
            .build();
    public static final VarBind<Guild, UniObjectNode, CustomEmoji, Span<CustomEmoji>> EMOJIS
            = TYPE.createBind("emojis")
            .extractAsArray()
            .andResolve(CustomEmoji::resolve)
            .intoSpan()
            .build();
    public static final VarBind<Guild, String, GuildFeature, Span<GuildFeature>> FEATURES
            = TYPE.createBind("features")
            .extractAsArray(StandardValueType.STRING)
            .andRemap(GuildFeature::valueOf)
            .intoSpan()
            .build();
    public static final VarBind<Guild, Integer, MFALevel, MFALevel> MFA_LEVEL
            = TYPE.createBind("mfa_level")
            .extractAs(StandardValueType.INTEGER)
            .andRemapRef(MFALevel::valueOf)
            .build();
    public static final VarBind<Guild, Long, User, User> OWNER_APPLICATION
            = TYPE.createBind("application_id")
            .extractAs(StandardValueType.LONG)
            .andResolveRef((guild, id) -> guild.requireFromContext(SnowflakeCache.class).getUser(id))
            .build();
    public static final VarBind<Guild, Long, Channel, Channel> SYSTEM_CHANNEL
            = TYPE.createBind("system_channel_id")
            .extractAs(StandardValueType.LONG)
            .andResolveRef((guild, id) -> guild.requireFromContext(SnowflakeCache.class).getChannel(id))
            .build();
    public static final VarBind<Guild, Integer, Set<SystemChannelFlag>, Set<SystemChannelFlag>> SYSTEM_CHANNEL_FLAGS
            = TYPE.createBind("system_channel_flags")
            .extractAs(StandardValueType.INTEGER)
            .andRemap(SystemChannelFlag::valueOf)
            .build();
    public static final VarBind<Guild, Long, Channel, Channel> RULES_CHANNEL
            = TYPE.createBind("rules_channel_id")
            .extractAs(StandardValueType.LONG)
            .andResolveRef((guild, id) -> guild.requireFromContext(SnowflakeCache.class).getChannel(id))
            .build();
    public static final VarBind<Guild, String, Instant, Instant> JOINED_AT
            = TYPE.createBind("joined_at")
            .extractAs(StandardValueType.STRING)
            .andRemap(Instant::parse)
            .build();
    public static final VarBind<Guild, Boolean, Boolean, Boolean> LARGE
            = TYPE.createBind("large")
            .extractAs(StandardValueType.BOOLEAN)
            .build();
    public static final VarBind<Guild, Boolean, Boolean, Boolean> UNAVAILABLE
            = TYPE.createBind("unavailable")
            .extractAs(StandardValueType.BOOLEAN)
            .build();
    public static final VarBind<Guild, Integer, Integer, Integer> MEMBER_COUNT
            = TYPE.createBind("member_count")
            .extractAs(StandardValueType.INTEGER)
            .build();
    public static final VarBind<Guild, UniObjectNode, VoiceState, Span<VoiceState>> VOICE_STATES
            = TYPE.createBind("voice_states")
            .extractAsArray()
            .andResolve(VoiceState::new)
            .intoSpan()
            .build();
    public static final VarBind<Guild, UniObjectNode, UniObjectNode, Span<UniObjectNode>> MEMBERS
            = TYPE.createBind("members")
            .extractAsArray()
            .asIdentities() // todo
            .intoSpan()
            .build();
    public static final VarBind<Guild, UniObjectNode, Channel, Span<Channel>> CHANNELS
            = TYPE.createBind("channels")
            .extractAsArray()
            .andResolve(Channel::resolve)
            .intoSpan()
            .build();
    public static final VarBind<Guild, UniObjectNode, UniObjectNode, Span<UniObjectNode>> PRESENCES
            = TYPE.createBind("presences")
            .extractAsArray()
            .asIdentities() // todo
            .intoSpan()
            .build();
    public static final VarBind<Guild, Integer, Integer, Integer> MAX_PRESENCES
            = TYPE.createBind("max_presences")
            .extractAs(StandardValueType.INTEGER)
            .build();
    public static final VarBind<Guild, Integer, Integer, Integer> MAX_MEMBERS
            = TYPE.createBind("max_members")
            .extractAs(StandardValueType.INTEGER)
            .build();
    public static final VarBind<Guild, String, URL, URL> VANITY_URL
            = TYPE.createBind("vanity_url_code")
            .extractAs(StandardValueType.STRING)
            .andRemap(code -> Polyfill.url("https://discord.gg/" + code))
            .build();
    public static final VarBind<Guild, String, String, String> DESCRIPTION
            = TYPE.createBind("description")
            .extractAs(StandardValueType.STRING)
            .build();
    public static final VarBind<Guild, String, URL, URL> BANNER_URL
            = TYPE.createBind("banner")
            .extractAs(StandardValueType.STRING)
            .andResolve((guild, hash) -> CDNEndpoint.GUILD_BANNER.complete(guild.getID(), hash, ImageType.PNG).getURL())
            .build();
    public static final VarBind<Guild, Integer, PremiumTier, PremiumTier> PREMIUM_TIER
            = TYPE.createBind("premium_tier")
            .extractAs(StandardValueType.INTEGER)
            .andRemapRef(PremiumTier::valueOf)
            .build();
    public static final VarBind<Guild, Integer, Integer, Integer> PREMIUM_SUBSCRIPTION_COUNT
            = TYPE.createBind("premium_subscription_count")
            .extractAs(StandardValueType.INTEGER)
            .build();
    public static final VarBind<Guild, String, Locale, Locale> PREFERRED_LOCALE
            = TYPE.createBind("preferred_locale")
            .extractAs(StandardValueType.STRING)
            .andRemap(Locale::forLanguageTag)
            .build();
    public static final VarBind<Guild, Long, Channel, Channel> PUBLIC_UPDATES_CHANNEL
            = TYPE.createBind("public_updates_channel_id")
            .extractAs(StandardValueType.LONG)
            .andResolveRef((guild, id) -> guild.requireFromContext(SnowflakeCache.class).getChannel(id))
            .build();
    public static final VarBind<Guild, Integer, Integer, Integer> MAX_VIDEO_CHANNEL_USERS
            = TYPE.createBind("max_video_channel_users")
            .extractAs(StandardValueType.INTEGER)
            .build();
    public static final VarBind<Guild, Integer, Integer, Integer> APPROXIMATE_MEMBER_COUNT
            = TYPE.createBind("approximate_member_count")
            .extractAs(StandardValueType.INTEGER)
            .build();
    public static final VarBind<Guild, Integer, Integer, Integer> APPROXIMATE_PRESENCE_COUNT
            = TYPE.createBind("approximate_presence_count")
            .extractAs(StandardValueType.INTEGER)
            .build();

    @Override
    public String getName() {
        return null;
    }

    private Guild(ContextualProvider context, UniObjectNode data) {
        super(context, data, EntityType.GUILD);
    }

    public static Guild resolve(ContextualProvider context, UniObjectNode data) {
        SnowflakeCache cache = context.requireFromContext(SnowflakeCache.class);
        long id = Snowflake.ID.getFrom(data);
        return cache.getGuild(id)
                .peek(it -> it.updateFrom(data))
                .orElseGet(() -> new Guild(context, data));
    }
}

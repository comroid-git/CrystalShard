package org.comroid.crystalshard.entity.guild;

import org.comroid.api.ContextualProvider;
import org.comroid.api.Named;
import org.comroid.crystalshard.SnowflakeCache;
import org.comroid.crystalshard.entity.EntityType;
import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.crystalshard.entity.channel.Channel;
import org.comroid.crystalshard.entity.user.User;
import org.comroid.crystalshard.model.guild.*;
import org.comroid.crystalshard.voice.VoiceRegion;
import org.comroid.mutatio.span.Span;
import org.comroid.uniform.node.impl.StandardValueType;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;

public final class Guild extends Snowflake.Abstract implements Named {
    @RootBind
    public static final GroupBind<Guild> TYPE = BASETYPE.rootGroup("guild");
    public static final VarBind<Guild, String, String, String> ICON_HASH
            = TYPE.createBind("icon")
            .extractAs(StandardValueType.STRING)
            .asIdentities()
            .onceEach()
            .build();
    public static final VarBind<Guild, String, String, String> SPLASH_HASH
            = TYPE.createBind("splash")
            .extractAs(StandardValueType.STRING)
            .asIdentities()
            .onceEach()
            .build();
    public static final VarBind<Guild, String, String, String> DISCOVERY_SPLASH_HASH
            = TYPE.createBind("discovery_splash_hash")
            .extractAs(StandardValueType.STRING)
            .asIdentities()
            .onceEach()
            .build();
    public static final VarBind<Guild, Boolean, Boolean, Boolean> IS_OWNER
            = TYPE.createBind("owner")
            .extractAs(StandardValueType.BOOLEAN)
            .asIdentities()
            .onceEach()
            .build();
    public static final VarBind<Guild, Long, User, User> OWNER
            = TYPE.createBind("owner_id")
            .extractAs(StandardValueType.LONG)
            .andResolve((guild, owner) -> guild.requireFromContext(SnowflakeCache.class)
                    .getUser(owner).get())
            .onceEach()
            .setRequired()
            .build();
    public static final VarBind<Guild, String, VoiceRegion, VoiceRegion> VOICE_REGION
            = TYPE.createBind("region")
            .extractAs(StandardValueType.STRING)
            .andResolve(VoiceRegion::find)
            .onceEach()
            .setRequired()
            .build();
    public static final VarBind<Guild, Long, Channel, Channel> AFK_CHANNEL
            = TYPE.createBind("afk_channel_id")
            .extractAs(StandardValueType.LONG)
            .andResolve((guild, channel) -> guild.requireFromContext(SnowflakeCache.class)
                    .getChannel(channel).get())
            .onceEach()
            .build();
    public static final VarBind<Guild, Integer, Integer, Integer> AFK_TIMEOUT
            = TYPE.createBind("afk_timeout")
            .extractAs(StandardValueType.INTEGER)
            .asIdentities()
            .onceEach()
            .setRequired()
            .build();
    public static final VarBind<Guild, Boolean, Boolean, Boolean> IS_WIDGET_ENABLED
            = TYPE.createBind("widget_enabled")
            .extractAs(StandardValueType.BOOLEAN)
            .asIdentities()
            .onceEach()
            .build();
    public static final VarBind<Guild, Long, Channel, Channel> WIDGET_CHANNEL_ID
            = TYPE.createBind("widget_channel_id")
            .extractAs(StandardValueType.LONG)
            .andResolve((guild, channel) -> guild.requireFromContext(SnowflakeCache.class)
                    .getChannel(channel).get())
            .onceEach()
            .build();
    public static final VarBind<Guild, Integer, VerificationLevel, VerificationLevel> VERIFICATION_LEVEL
            = TYPE.createBind("verification_level")
            .extractAs(StandardValueType.INTEGER)
            .andRemapRef(VerificationLevel::valueOf)
            .onceEach()
            .setRequired()
            .build();
    public static final VarBind<Guild, Integer, DefaultMessageNotificationLevel, DefaultMessageNotificationLevel> DEFAULT_NOTIFICATION_LEVEL
            = TYPE.createBind("default_message_notifications")
            .extractAs(StandardValueType.INTEGER)
            .andRemapRef(DefaultMessageNotificationLevel::valueOf)
            .onceEach()
            .setRequired()
            .build();
    public static final VarBind<Guild, Integer, ExplicitContentFilter, ExplicitContentFilter> EXPLICIT_CONTENT_FILTER_LEVEL
            = TYPE.createBind("explicit_content_filter")
            .extractAs(StandardValueType.INTEGER)
            .andRemapRef(ExplicitContentFilter::valueOf)
            .onceEach()
            .setRequired()
            .build();
    public static final VarBind<Guild, UniObjectNode, Role, Span<Role>> ROLES
            = TYPE.createBind("roles")
            .extractAsArray()
            .andConstruct(Role.TYPE)
            .intoSpan()
            .setRequired()
            .build();
    public static final VarBind<Guild, UniObjectNode, CustomEmoji, Span<CustomEmoji>> EMOJIS
            = TYPE.createBind("emojis")
            .extractAsArray()
            .andConstruct(CustomEmoji.TYPE)
            .intoSpan()
            .setRequired()
            .build();
    public static final VarBind<Guild, String, GuildFeature, Span<GuildFeature>> FEATURES
            = TYPE.createBind("features")
            .extractAsArray(StandardValueType.STRING)
            .andRemap(GuildFeature::valueOf)
            .intoSpan()
            .setRequired()
            .build();
    public static final VarBind<Guild, Integer, MFALevel, MFALevel> MFA_LEVEL
            = TYPE.createBind("mfa_level")
            .extractAs(StandardValueType.INTEGER)
            .andRemapRef(MFALevel::valueOf)
            .onceEach()
            .setRequired()
            .build();

    @Override
    public String getName() {
        return null;
    }

    public Guild(ContextualProvider context, UniObjectNode data) {
        super(context, data, EntityType.GUILD);
    }
}

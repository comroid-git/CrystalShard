package org.comroid.crystalshard.entity.user;

import org.comroid.api.BitmaskEnum;
import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.Bot;
import org.comroid.crystalshard.cdn.CDNEndpoint;
import org.comroid.crystalshard.cdn.ImageType;
import org.comroid.crystalshard.entity.EntityType;
import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.crystalshard.entity.SnowflakeCache;
import org.comroid.crystalshard.entity.channel.PrivateTextChannel;
import org.comroid.crystalshard.entity.message.Message;
import org.comroid.crystalshard.model.message.MessageBuilder;
import org.comroid.crystalshard.model.message.MessageTarget;
import org.comroid.crystalshard.model.user.PremiumType;
import org.comroid.crystalshard.rest.Endpoint;
import org.comroid.mutatio.ref.Reference;
import org.comroid.restless.REST;
import org.comroid.restless.body.BodyBuilderType;
import org.comroid.uniform.node.UniNode;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.util.StandardValueType;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public final class User extends Snowflake.Abstract implements MessageTarget {
    @RootBind
    public static final GroupBind<User> TYPE
            = BASETYPE.subGroup("user", User::resolve);
    public static final VarBind<User, String, String, String> USERNAME
            = TYPE.createBind("username")
            .extractAs(StandardValueType.STRING)
            .build();
    public static final VarBind<User, String, String, String> DISCRIMINATOR
            = TYPE.createBind("discriminator")
            .extractAs(StandardValueType.STRING)
            .build();
    public static final VarBind<User, String, String, String> AVATAR_HASH
            = TYPE.createBind("avatar")
            .extractAs(StandardValueType.STRING)
            .build();
    public static final VarBind<User, Boolean, Boolean, Boolean> IS_BOT
            = TYPE.createBind("bot")
            .extractAs(StandardValueType.BOOLEAN)
            .build();
    public static final VarBind<User, Boolean, Boolean, Boolean> IS_SYSTEM
            = TYPE.createBind("system")
            .extractAs(StandardValueType.BOOLEAN)
            .build();
    public static final VarBind<User, Boolean, Boolean, Boolean> MFA_ENABLED
            = TYPE.createBind("mfa_enabled")
            .extractAs(StandardValueType.BOOLEAN)
            .build();
    public static final VarBind<User, String, Locale, Locale> LOCALE
            = TYPE.createBind("locale")
            .extractAs(StandardValueType.STRING)
            .andRemap(Locale::forLanguageTag)
            .build();
    public static final VarBind<User, Boolean, Boolean, Boolean> IS_VERIFIED
            = TYPE.createBind("verified")
            .extractAs(StandardValueType.BOOLEAN)
            .build();
    public static final VarBind<User, String, String, String> EMAIL
            = TYPE.createBind("email")
            .extractAs(StandardValueType.STRING)
            .build();
    public static final VarBind<User, Integer, Set<Flags>, Set<Flags>> FLAGS
            = TYPE.createBind("flags")
            .extractAs(StandardValueType.INTEGER)
            .andRemap(Flags::valueOf)
            .build();
    public static final VarBind<User, Integer, PremiumType, PremiumType> PREMIUM_TYPE
            = TYPE.createBind("premium_type")
            .extractAs(StandardValueType.INTEGER)
            .andRemapRef(PremiumType::valueOf)
            .build();
    public static final VarBind<User, Integer, Set<Flags>, Set<Flags>> PUBLIC_FLAGS
            = TYPE.createBind("public_flags")
            .extractAs(StandardValueType.INTEGER)
            .andRemap(Flags::valueOf)
            .build();
    public final Reference<String> username = getComputedReference(USERNAME);
    public final Reference<String> discriminator = getComputedReference(DISCRIMINATOR);
    public final Reference<String> avatarHash = getComputedReference(AVATAR_HASH);
    public final Reference<Boolean> isBot = getComputedReference(IS_BOT);
    public final Reference<Boolean> isSystemUser = getComputedReference(IS_SYSTEM);
    public final Reference<Boolean> isMfaEnabled = getComputedReference(MFA_ENABLED);
    public final Reference<Locale> preferredLocale = getComputedReference(LOCALE);
    public final Reference<Boolean> isVerified = getComputedReference(IS_VERIFIED);
    public final Reference<String> email = getComputedReference(EMAIL);
    public final Reference<PremiumType> premiumType = getComputedReference(PREMIUM_TYPE);
    public final Reference<Set<Flags>> allFlags = getComputedReference(FLAGS);
    public final Reference<Set<Flags>> publicFlags = getComputedReference(PUBLIC_FLAGS);

    public String getUsername() {
        return username.assertion();
    }

    public String getDiscriminator() {
        return discriminator.assertion();
    }

    public URL getAvatarURL(ImageType imageType) {
        return avatarHash.into(hash -> CDNEndpoint.USER_AVATAR.complete(getID(), hash, imageType)).getURL();
    }

    public boolean isBot() {
        return isBot.orElse(false);
    }

    public boolean isSystemUser() {
        return isSystemUser.orElse(false);
    }

    public boolean isMfaEnabled() {
        return isMfaEnabled.orElse(false);
    }

    public Locale getPreferredLocale() {
        return preferredLocale.assertion();
    }

    public boolean isVerified() {
        return isVerified.orElse(false);
    }

    public String getEMail() {
        return email.assertion();
    }

    public PremiumType getPremiumType() {
        return premiumType.assertion();
    }

    public Set<Flags> getAllFlags() {
        return allFlags.assertion();
    }

    public Set<Flags> getPublicFlags() {
        return publicFlags.assertion();
    }

    public CompletableFuture<PrivateTextChannel> openPrivateChannel() {
        return requireFromContext(Bot.class).newRequest(
                REST.Method.POST,
                Endpoint.PRIVATE_CHANNELS,
                BodyBuilderType.OBJECT,
                obj -> obj.put("recipient_id", getID()),
                PrivateTextChannel.TYPE
        );
    }

    @Override
    public CompletableFuture<Message> executeMessage(MessageBuilder builder) {
        return openPrivateChannel().thenCompose(ptc -> ptc.executeMessage(builder));
    }

    private User(ContextualProvider context, UniObjectNode data) {
        super(context, data, EntityType.USER);
    }

    public static User resolve(ContextualProvider context, UniNode data) {
        return Snowflake.resolve(context, data, SnowflakeCache::getUser, User::new);
    }

    @SuppressWarnings("PointlessBitwiseExpression")
    public enum Flags implements BitmaskEnum<Flags> {
        DISCORD_EMPLOYEE(1 << 0),
        PARTNERED_SERVER_OWNER(1 << 1),
        EARLY_SUPPORTER(1 << 9),
        TEAM_USER(1 << 10),
        SYSTEM_USER(1 << 12),

        BUG_HUNTER_LEVEL_1(1 << 3),
        BUG_HUNTER_LEVEL_2(1 << 14),

        HYPESQUAD_EVENTS(1 << 2),
        HOUSE_BRAVERY(1 << 6),
        HOUSE_BRILLIANCE(1 << 7),
        HOUSE_BALANCE(1 << 8),

        VERIFIED_BOT(1 << 16),
        EARLY_VERIFIED_BOT_DEVELOPER(1 << 17);

        private final int value;

        @Override
        public @NotNull Integer getValue() {
            return value;
        }

        Flags(int value) {
            this.value = value;
        }

        public static Set<Flags> valueOf(int mask) {
            return BitmaskEnum.valueOf(mask, Flags.class);
        }
    }
}

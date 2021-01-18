package org.comroid.crystalshard.entity.user;

import org.comroid.api.BitmaskEnum;
import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.Bot;
import org.comroid.crystalshard.entity.EntityType;
import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.crystalshard.entity.SnowflakeCache;
import org.comroid.crystalshard.entity.channel.PrivateTextChannel;
import org.comroid.crystalshard.model.MessageTarget;
import org.comroid.crystalshard.model.user.PremiumType;
import org.comroid.crystalshard.rest.Endpoint;
import org.comroid.restless.REST;
import org.comroid.restless.body.BodyBuilderType;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.uniform.node.impl.StandardValueType;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;

import java.util.Locale;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public final class User extends Snowflake.Abstract implements MessageTarget {
    @RootBind
    public static final GroupBind<User> TYPE
            = BASETYPE.subGroup("user");
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

    @Override
    public CompletableFuture<PrivateTextChannel> getTargetChannel() {
        return requireFromContext(Bot.class).newRequest(
                REST.Method.POST,
                Endpoint.PRIVATE_CHANNELS,
                PrivateTextChannel.TYPE,
                BodyBuilderType.OBJECT,
                obj -> obj.put("recipient_id", getID())
        );
    }

    private User(ContextualProvider context, UniObjectNode data) {
        super(context, data, EntityType.USER);
    }

    public static User resolve(ContextualProvider context, UniObjectNode data) {
        return Snowflake.resolve(context, data, SnowflakeCache::getUser, User::new);
    }

    @SuppressWarnings("PointlessBitwiseExpression")
    public enum Flags implements BitmaskEnum<Flags> {
        DISCORD_EMPLOYEE(1<<0),
        PARTNERED_SERVER_OWNER(1<<1),
        EARLY_SUPPORTER(1<<9),
        TEAM_USER(1<<10),
        SYSTEM_USER(1<<12),

        BUG_HUNTER_LEVEL_1(1<<3),
        BUG_HUNTER_LEVEL_2(1<<14),

        HYPESQUAD_EVENTS(1<<2),
        HOUSE_BRAVERY(1<<6),
        HOUSE_BRILLIANCE(1<<7),
        HOUSE_BALANCE(1<<8),

        VERIFIED_BOT(1<<16),
        EARLY_VERIFIED_BOT_DEVELOPER(1<<17);

        private final int value;

        @Override
        public int getValue() {
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

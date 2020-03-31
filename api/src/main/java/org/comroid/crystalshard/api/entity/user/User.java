package org.comroid.crystalshard.api.entity.user;

import java.net.URL;
import java.util.Collection;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.comroid.crystalshard.adapter.Adapter;
import org.comroid.crystalshard.adapter.MainAPI;
import org.comroid.crystalshard.api.Discord;
import org.comroid.crystalshard.api.entity.Snowflake;
import org.comroid.crystalshard.api.entity.channel.PrivateTextChannel;
import org.comroid.crystalshard.api.entity.guild.Guild;
import org.comroid.crystalshard.api.entity.user.User.Bind;
import org.comroid.crystalshard.api.event.EventHandler;
import org.comroid.crystalshard.api.event.multipart.user.UserEvent;
import org.comroid.crystalshard.api.model.Mentionable;
import org.comroid.crystalshard.api.model.message.MessageAuthor;
import org.comroid.crystalshard.api.model.message.Messageable;
import org.comroid.crystalshard.api.model.user.Yourself;
import org.comroid.crystalshard.core.cache.Cacheable;
import org.comroid.crystalshard.core.rest.DiscordEndpoint;
import org.comroid.crystalshard.util.annotation.IntroducedBy;
import org.comroid.crystalshard.util.model.FileType;
import org.comroid.crystalshard.util.model.ImageHelper;
import org.comroid.uniform.http.REST;
import org.comroid.varbind.VarBind.Location;
import org.comroid.varbind.VarCarrier;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.Nullable;

import static org.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;
import static org.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.PRODUCTION;

@MainAPI
@Location(Bind.class)
public interface User
        extends Messageable, MessageAuthor, Mentionable, Snowflake, Cacheable,
        VarCarrier<JSON, JSONObject, JSONArray>, EventHandler<UserEvent> {
    @IntroducedBy(value = API,
                  docs = "https://discordapp.com/developers/docs/resources/user#get-user")
    static CompletableFuture<User> requestUser(final Discord api, long id) {
        return Discord.request(DiscordEndpoint.USER, id)
                                         .method(REST.Method.GET)
                                         .executeAsObject(data -> Adapter.require(User.class,
                                                                                  api,
                                                                                  data
                                         ));
    }

    @SuppressWarnings("PointlessBitwiseExpression")
    final class Flags {
        public static final int NONE = 0;

        public static final int DISCORD_EMPLOYEE = 1 << 0;

        public static final int DISCORD_PARTNER = 1 << 1;

        public static final int HYPESQUAD_EVENTS = 1 << 2;

        public static final int BUG_HUNTER = 1 << 3;

        public static final int HOUSE_BRAVERY = 1 << 6;

        public static final int HOUSE_BRILLIANCE = 1 << 7;

        public static final int HOUSE_BALANCE = 1 << 8;

        public static final int EARLY_SUPPORTER = 1 << 9;

        public static final int TEAM_USER = 1 << 10;
    }

    default String getNicknameMentionTag() {
        return null; // todo
    }

    default URL getAvatarURL() {
        return wrapBindingValue(Bind.AVATAR_HASH).map(hash -> ImageHelper.USER_AVATAR.url(FileType.PNG,
                                                                                          getID(),
                                                                                          hash
        ))
                                                 .orElseGet(() -> ImageHelper.DEFAULT_USER_AVATAR.url(FileType.PNG,
                                                                                                      getDiscriminator()
                                                 ));
    }

    default String getDiscriminator() {
        return getBindingValue(Bind.DISCRIMINATOR);
    }

    default boolean isBot() {
        return getBindingValue(Bind.BOT);
    }

    default Optional<Locale> getLocale() {
        return wrapBindingValue(Bind.LOCALE);
    }

    default Optional<String> getEMailAddress() {
        return wrapBindingValue(Bind.EMAIL);
    }

    default @MagicConstant(flagsFromClass = Flags.class)
    int getFlags() {
        return getBindingValue(Bind.FLAGS);
    }

    default Optional<PremiumType> getPremiumType() {
        return wrapBindingValue(Bind.PREMIUM_TYPE);
    }

    @IntroducedBy(PRODUCTION)
    default String getDiscriminatedName() {
        return getUsername() + '#' + getDiscriminator();
    }

    default String getUsername() {
        return getBindingValue(Bind.USERNAME);
    }

    default boolean isYourself() {
        return this instanceof Yourself;
    }

    default boolean hasMFA() {
        return getBindingValue(Bind.MFA);
    }

    default Optional<Boolean> isVerified() {
        return wrapBindingValue(Bind.VERIFIED);
    }

    @IntroducedBy(value = API,
                  docs = "https://discordapp.com/developers/docs/resources/user#create-dm")
    CompletableFuture<PrivateTextChannel> openPrivateMessageChannel();

    Optional<GuildMember> asGuildMember(Guild guild);

    interface Bind extends Snowflake.Bind {
        JSONBinding.OneStage<String>               USERNAME      = identity("username",
                                                                            JSONObject::getString
        );
        JSONBinding.OneStage<String>               DISCRIMINATOR = identity("discriminator",
                                                                            JSONObject::getString
        );
        JSONBinding.OneStage<String>               AVATAR_HASH   = identity("avatar",
                                                                            JSONObject::getString
        );
        JSONBinding.OneStage<Boolean>              BOT           = identity("bot",
                                                                            JSONObject::getBoolean
        );
        JSONBinding.OneStage<Boolean>              MFA           = identity("mfa_enabled",
                                                                            JSONObject::getBoolean
        );
        JSONBinding.TwoStage<String, Locale>       LOCALE        = simple("locale",
                                                                          JSONObject::getString,
                                                                          Locale::forLanguageTag
        );
        JSONBinding.OneStage<Boolean>              VERIFIED      = identity("verified",
                                                                            JSONObject::getBoolean
        );
        JSONBinding.OneStage<String>               EMAIL         = identity("email",
                                                                            JSONObject::getString
        );
        JSONBinding.OneStage<Integer>              FLAGS         = identity("flags",
                                                                            JSONObject::getInteger
        );
        JSONBinding.TwoStage<Integer, PremiumType> PREMIUM_TYPE  = simple("premium_type",
                                                                          JSONObject::getInteger,
                                                                          PremiumType::valueOf
        );
    }

    @MainAPI
    @JSONBindingLocation(Connection.JSON.class)
    interface Connection extends JsonDeserializable {
        default String getID() {
            return getBindingValue(JSON.ID);
        }

        default String getName() {
            return getBindingValue(JSON.NAME);
        }

        default String getType() {
            return getBindingValue(JSON.TYPE);
        }

        default boolean isRevoked() {
            return getBindingValue(JSON.REVOKED);
        }

        default Collection<Guild.Integration> getIntegrations() {
            return getBindingValue(JSON.INTEGRATIONS);
        }

        default boolean isVerified() {
            return getBindingValue(JSON.VERIFIED);
        }

        default Visibility getVisibility() {
            return getBindingValue(JSON.VISIBILITY);
        }

        default boolean hasFriendSync() {
            return getBindingValue(JSON.FRIEND_SYNC);
        }

        default boolean showActivities() {
            return getBindingValue(JSON.SHOW_ACTIVITY);
        }

        interface JSON {
            JSONBinding.OneStage<String>                        ID            = identity("id",
                                                                                         JSONObject::getString
            );
            JSONBinding.OneStage<String>                        NAME          = identity("name",
                                                                                         JSONObject::getString
            );
            JSONBinding.OneStage<String>                        TYPE          = identity("type",
                                                                                         JSONObject::getString
            );
            JSONBinding.OneStage<Boolean>                       REVOKED       = identity("revoked",
                                                                                         JSONObject::getBoolean
            );
            JSONBinding.TriStage<JSONObject, Guild.Integration> INTEGRATIONS  = serializableCollection(
                    "integrations",
                    Guild.Integration.class
            );
            JSONBinding.OneStage<Boolean>                       VERIFIED      = identity("verified",
                                                                                         JSONObject::getBoolean
            );
            JSONBinding.OneStage<Boolean>                       FRIEND_SYNC   = identity(
                    "friend_sync",
                    JSONObject::getBoolean
            );
            JSONBinding.OneStage<Boolean>                       SHOW_ACTIVITY = identity("show_activity",
                                                                                         JSONObject::getBoolean
            );
            JSONBinding.TwoStage<Integer, Visibility>           VISIBILITY    = simple("visibility",
                                                                                       JSONObject::getInteger,
                                                                                       Visibility::valueOf
            );
        }

        enum Visibility {
            NONE(0),

            EVERYONE(1);

            public static @Nullable Visibility valueOf(int value) {
                for (Visibility visibility : values())
                    if (visibility.value == value) return visibility;

                return null;
            }

            public final int value;

            Visibility(int value) {
                this.value = value;
            }
        }
    }

    enum PremiumType {
        NITRO_CLASSIC(1),

        NITRO(2);

        public static @Nullable PremiumType valueOf(int value) {
            for (PremiumType type : values())
                if (type.value == value) return type;

            return null;
        }

        public final int value;

        PremiumType(int value) {
            this.value = value;
        }
    }
}

package de.comroid.crystalshard.api.entity.user;

import java.net.URL;
import java.util.Collection;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import de.comroid.crystalshard.adapter.Adapter;
import de.comroid.crystalshard.adapter.MainAPI;
import de.comroid.crystalshard.api.Discord;
import de.comroid.crystalshard.api.entity.Snowflake;
import de.comroid.crystalshard.api.entity.channel.PrivateTextChannel;
import de.comroid.crystalshard.api.entity.guild.Guild;
import de.comroid.crystalshard.api.event.user.UserEvent;
import de.comroid.crystalshard.api.listener.ListenerSpec;
import de.comroid.crystalshard.api.listener.model.ListenerAttachable;
import de.comroid.crystalshard.api.model.Mentionable;
import de.comroid.crystalshard.api.model.message.MessageAuthor;
import de.comroid.crystalshard.api.model.message.Messageable;
import de.comroid.crystalshard.core.cache.Cacheable;
import de.comroid.crystalshard.core.rest.DiscordEndpoint;
import de.comroid.crystalshard.core.rest.RestMethod;
import de.comroid.crystalshard.util.annotation.IntroducedBy;
import de.comroid.crystalshard.util.model.FileType;
import de.comroid.crystalshard.util.model.ImageHelper;
import de.comroid.crystalshard.util.model.serialization.JSONBinding;
import de.comroid.crystalshard.util.model.serialization.JsonDeserializable;
import de.comroid.crystalshard.util.model.serialization.JSONBindingLocation;

import com.alibaba.fastjson.JSONObject;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.Nullable;

import static de.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;
import static de.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.PRODUCTION;
import static de.comroid.crystalshard.util.model.serialization.JSONBinding.identity;
import static de.comroid.crystalshard.util.model.serialization.JSONBinding.serializableCollection;
import static de.comroid.crystalshard.util.model.serialization.JSONBinding.simple;

@MainAPI
@JSONBindingLocation(User.JSON.class)
public interface User extends Messageable, MessageAuthor, Mentionable, Snowflake, Cacheable, ListenerAttachable<ListenerSpec.AttachableTo.User<? extends UserEvent>>, JsonDeserializable {
    default String getUsername() {
        return getBindingValue(JSON.USERNAME);
    }

    default String getDiscriminator() {
        return getBindingValue(JSON.DISCRIMINATOR);
    }

    default URL getAvatarURL() {
        return wrapBindingValue(JSON.AVATAR_HASH)
                .map(hash -> ImageHelper.USER_AVATAR.url(FileType.PNG, getID(), hash))
                .orElseGet(() -> ImageHelper.DEFAULT_USER_AVATAR.url(FileType.PNG, getDiscriminator()));
    }

    default boolean isBot() {
        return getBindingValue(JSON.BOT);
    }

    default boolean hasMFA() {
        return getBindingValue(JSON.MFA);
    }

    default Optional<Locale> getLocale() {
        return wrapBindingValue(JSON.LOCALE);
    }

    default Optional<Boolean> isVerified() {
        return wrapBindingValue(JSON.VERIFIED);
    }

    default Optional<String> getEMailAddress() {
        return wrapBindingValue(JSON.EMAIL);
    }

    default @MagicConstant(flagsFromClass = Flags.class) int getFlags() {
        return getBindingValue(JSON.FLAGS);
    }

    default Optional<PremiumType> getPremiumType() {
        return wrapBindingValue(JSON.PREMIUM_TYPE);
    }

    @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/user#create-dm")
    CompletableFuture<PrivateTextChannel> openPrivateMessageChannel();

    Optional<GuildMember> asGuildMember(Guild guild);

    @IntroducedBy(PRODUCTION)
    default String getDiscriminatedName() {
        return getUsername() +'#'+ getDiscriminator();
    }

    @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/user#get-user")
    static CompletableFuture<User> requestUser(final Discord api, long id) {
        return Adapter.<User>request(api)
                .endpoint(DiscordEndpoint.USER, id)
                .method(RestMethod.GET)
                .executeAsObject(data -> Adapter.require(User.class, api, data));
    }

    interface JSON extends Snowflake.JSON {
        JSONBinding.OneStage<String> USERNAME = identity("username", JSONObject::getString);
        JSONBinding.OneStage<String> DISCRIMINATOR = identity("discriminator", JSONObject::getString);
        JSONBinding.OneStage<String> AVATAR_HASH = identity("avatar", JSONObject::getString);
        JSONBinding.OneStage<Boolean> BOT = identity("bot", JSONObject::getBoolean);
        JSONBinding.OneStage<Boolean> MFA = identity("mfa_enabled", JSONObject::getBoolean);
        JSONBinding.TwoStage<String, Locale> LOCALE = simple("locale", JSONObject::getString, Locale::forLanguageTag);
        JSONBinding.OneStage<Boolean> VERIFIED = identity("verified", JSONObject::getBoolean);
        JSONBinding.OneStage<String> EMAIL = identity("email", JSONObject::getString);
        JSONBinding.OneStage<Integer> FLAGS = identity("flags", JSONObject::getInteger);
        JSONBinding.TwoStage<Integer, PremiumType> PREMIUM_TYPE = simple("premium_type", JSONObject::getInteger, PremiumType::valueOf);
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

        default boolean hasFriendSync() {
            return getBindingValue(JSON.FRIEND_SYNC);
        }

        default boolean showActivities() {
            return getBindingValue(JSON.SHOW_ACTIVITY);
        }

        default Visibility getVisibility() {
            return getBindingValue(JSON.VISIBILITY);
        }

        interface JSON {
            JSONBinding.OneStage<String> ID = identity("id", JSONObject::getString);
            JSONBinding.OneStage<String> NAME = identity("name", JSONObject::getString);
            JSONBinding.OneStage<String> TYPE = identity("type", JSONObject::getString);
            JSONBinding.OneStage<Boolean> REVOKED = identity("revoked", JSONObject::getBoolean);
            JSONBinding.TriStage<JSONObject, Guild.Integration> INTEGRATIONS = serializableCollection("integrations", Guild.Integration.class);
            JSONBinding.OneStage<Boolean> VERIFIED = identity("verified", JSONObject::getBoolean);
            JSONBinding.OneStage<Boolean> FRIEND_SYNC = identity("friend_sync", JSONObject::getBoolean);
            JSONBinding.OneStage<Boolean> SHOW_ACTIVITY = identity("show_activity", JSONObject::getBoolean);
            JSONBinding.TwoStage<Integer, Visibility> VISIBILITY = simple("visibility", JSONObject::getInteger, Visibility::valueOf);
        }

        enum Visibility {
            NONE(0),

            EVERYONE(1);

            public final int value;

            Visibility(int value) {
                this.value = value;
            }

            public static @Nullable Visibility valueOf(int value) {
                for (Visibility visibility : values())
                    if (visibility.value == value)
                        return visibility;

                return null;
            }
        }
    }

    @SuppressWarnings("PointlessBitwiseExpression") final class Flags {
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

    enum PremiumType {
        NITRO_CLASSIC(1),

        NITRO(2);

        public final int value;

        PremiumType(int value) {
            this.value = value;
        }

        public static @Nullable PremiumType valueOf(int value) {
            for (PremiumType type : values())
                if (type.value == value)
                    return type;

            return null;
        }
    }
}

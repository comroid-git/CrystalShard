package de.comroid.crystalshard.api.entity.user;

import java.net.URL;
import java.util.Collection;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import de.comroid.crystalshard.adapter.Adapter;
import de.comroid.crystalshard.api.Discord;
import de.comroid.crystalshard.api.entity.Snowflake;
import de.comroid.crystalshard.api.entity.channel.PrivateTextChannel;
import de.comroid.crystalshard.api.entity.guild.Guild;
import de.comroid.crystalshard.api.event.user.UserEvent;
import de.comroid.crystalshard.api.listener.AttachableTo;
import de.comroid.crystalshard.api.listener.model.ListenerAttachable;
import de.comroid.crystalshard.api.model.Mentionable;
import de.comroid.crystalshard.api.model.message.MessageAuthor;
import de.comroid.crystalshard.api.model.message.Messageable;
import de.comroid.crystalshard.core.api.cache.Cacheable;
import de.comroid.crystalshard.core.api.rest.DiscordEndpoint;
import de.comroid.crystalshard.core.api.rest.RestMethod;
import de.comroid.crystalshard.util.annotation.IntroducedBy;
import de.comroid.crystalshard.util.model.FileType;
import de.comroid.crystalshard.util.model.ImageHelper;
import de.comroid.crystalshard.util.model.serialization.JsonDeserializable;
import de.comroid.crystalshard.util.model.serialization.JsonBinding;
import de.comroid.crystalshard.util.model.serialization.JsonTraits;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.Nullable;

import static de.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;
import static de.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.PRODUCTION;
import static de.comroid.crystalshard.util.model.serialization.JsonBinding.identity;
import static de.comroid.crystalshard.util.model.serialization.JsonBinding.simple;
import static de.comroid.crystalshard.util.model.serialization.JsonBinding.underlyingCollective;

@JsonTraits(User.Trait.class)
public interface User extends Messageable, MessageAuthor, Mentionable, Snowflake, Cacheable, ListenerAttachable<AttachableTo.User<? extends UserEvent>>, JsonDeserializable {
    default String getUsername() {
        return getTraitValue(Trait.USERNAME);
    }

    default String getDiscriminator() {
        return getTraitValue(Trait.DISCRIMINATOR);
    }

    default URL getAvatarURL() {
        return wrapTraitValue(Trait.AVATAR_HASH)
                .map(hash -> ImageHelper.USER_AVATAR.url(FileType.PNG, getID(), hash))
                .orElseGet(() -> ImageHelper.DEFAULT_USER_AVATAR.url(FileType.PNG, getDiscriminator()));
    }

    default boolean isBot() {
        return getTraitValue(Trait.BOT);
    }

    default boolean hasMFA() {
        return getTraitValue(Trait.MFA);
    }

    default Optional<Locale> getLocale() {
        return wrapTraitValue(Trait.LOCALE);
    }

    default Optional<Boolean> isVerified() {
        return wrapTraitValue(Trait.VERIFIED);
    }

    default Optional<String> getEMailAddress() {
        return wrapTraitValue(Trait.EMAIL);
    }

    default @MagicConstant(flagsFromClass = Flags.class) int getFlags() {
        return getTraitValue(Trait.FLAGS);
    }

    default Optional<PremiumType> getPremiumType() {
        return wrapTraitValue(Trait.PREMIUM_TYPE);
    }

    @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/user#create-dm")
    CompletableFuture<PrivateTextChannel> openPrivateMessageChannel();

    Optional<GuildMember> asGuildMember(Guild guild);

    @IntroducedBy(PRODUCTION)
    default String getDiscriminatedName() {
        return getUsername() +'#'+ getDiscriminator();
    }

    @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/user#get-user")
    static CompletableFuture<User> requestUser(Discord api, long id) {
        return Adapter.<User>request(api)
                .endpoint(DiscordEndpoint.USER, id)
                .method(RestMethod.GET)
                .executeAs(data -> api.getCacheManager()
                        .updateOrCreateAndGet(User.class, id, data));
    }

    interface Trait extends Snowflake.Trait {
        JsonBinding<String, String> USERNAME = identity(JsonNode::asText, "username");
        JsonBinding<String, String> DISCRIMINATOR = identity(JsonNode::asText, "discriminator");
        JsonBinding<String, String> AVATAR_HASH = identity(JsonNode::asText, "avatar");
        JsonBinding<Boolean, Boolean> BOT = identity(JsonNode::asBoolean, "bot");
        JsonBinding<Boolean, Boolean> MFA = identity(JsonNode::asBoolean, "mfa_enabled");
        JsonBinding<String, Locale> LOCALE = simple(JsonNode::asText, "locale", Locale::forLanguageTag);
        JsonBinding<Boolean, Boolean> VERIFIED = identity(JsonNode::asBoolean, "verified");
        JsonBinding<String, String> EMAIL = identity(JsonNode::asText, "email");
        JsonBinding<Integer, Integer> FLAGS = identity(JsonNode::asInt, "flags");
        JsonBinding<Integer, PremiumType> PREMIUM_TYPE = simple(JsonNode::asInt, "premium_type", PremiumType::valueOf);
    }

    @JsonTraits(Connection.Trait.class)
    interface Connection extends JsonDeserializable {
        default String getID() {
            return getTraitValue(Trait.ID);
        }

        default String getName() {
            return getTraitValue(Trait.NAME);
        }

        default String getType() {
            return getTraitValue(Trait.TYPE);
        }

        default boolean isRevoked() {
            return getTraitValue(Trait.REVOKED);
        }

        default Collection<Guild.Integration> getIntegrations() {
            return getTraitValue(Trait.INTEGRATIONS);
        }

        default boolean isVerified() {
            return getTraitValue(Trait.VERIFIED);
        }

        default boolean hasFriendSync() {
            return getTraitValue(Trait.FRIEND_SYNC);
        }

        default boolean showActivities() {
            return getTraitValue(Trait.SHOW_ACTIVITY);
        }

        default Visibility getVisibility() {
            return getTraitValue(Trait.VISIBILITY);
        }

        interface Trait {
            JsonBinding<String, String> ID = identity(JsonNode::asText, "id");
            JsonBinding<String, String> NAME = identity(JsonNode::asText, "name");
            JsonBinding<String, String> TYPE = identity(JsonNode::asText, "type");
            JsonBinding<Boolean, Boolean> REVOKED = identity(JsonNode::asBoolean, "revoked");
            JsonBinding<ArrayNode, Collection<Guild.Integration>> INTEGRATIONS = underlyingCollective("integrations", Guild.Integration.class);
            JsonBinding<Boolean, Boolean> VERIFIED = identity(JsonNode::asBoolean, "verified");
            JsonBinding<Boolean, Boolean> FRIEND_SYNC = identity(JsonNode::asBoolean, "friend_sync");
            JsonBinding<Boolean, Boolean> SHOW_ACTIVITY = identity(JsonNode::asBoolean, "show_activity");
            JsonBinding<Integer, Visibility> VISIBILITY = simple(JsonNode::asInt, "visibility", Visibility::valueOf);
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

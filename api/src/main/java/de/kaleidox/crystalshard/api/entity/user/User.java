package de.kaleidox.crystalshard.api.entity.user;

import java.net.URL;
import java.util.Collection;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import de.kaleidox.crystalshard.adapter.Adapter;
import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.api.entity.Snowflake;
import de.kaleidox.crystalshard.api.entity.channel.PrivateTextChannel;
import de.kaleidox.crystalshard.api.entity.guild.Guild;
import de.kaleidox.crystalshard.api.event.user.UserEvent;
import de.kaleidox.crystalshard.api.listener.model.ListenerAttachable;
import de.kaleidox.crystalshard.api.listener.user.UserAttachableListener;
import de.kaleidox.crystalshard.api.model.Mentionable;
import de.kaleidox.crystalshard.api.model.message.MessageAuthor;
import de.kaleidox.crystalshard.api.model.message.Messageable;
import de.kaleidox.crystalshard.core.api.cache.Cacheable;
import de.kaleidox.crystalshard.core.api.rest.DiscordEndpoint;
import de.kaleidox.crystalshard.core.api.rest.RestMethod;
import de.kaleidox.crystalshard.util.annotation.IntroducedBy;

import org.intellij.lang.annotations.MagicConstant;

import static de.kaleidox.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;

public interface User extends Messageable, MessageAuthor, Mentionable, Snowflake, Cacheable, ListenerAttachable<UserAttachableListener<? extends UserEvent>> {
    String getUsername();

    String getDiscriminator();

    URL getAvatarURL();

    boolean isBot();

    boolean hasMFA();

    Optional<Locale> getLocale();

    Optional<Boolean> isVerified();

    Optional<String> getEMailAddress();

    @MagicConstant(flagsFromClass = Flags.class)
    int getFlags();

    Optional<PremiumType> getPremiumType();

    @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/user#create-dm")
    CompletableFuture<PrivateTextChannel> openPrivateMessageChannel();

    Optional<GuildMember> asGuildMember(Guild guild);

    @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/user#get-user")
    static CompletableFuture<User> requestUser(Discord api, long id) {
        return Adapter.<User>request(api)
                .endpoint(DiscordEndpoint.USER, id)
                .method(RestMethod.GET)
                .executeAs(data -> api.getCacheManager()
                        .updateOrCreateAndGet(User.class, id, data));
    }

    interface Connection {
        String getID();

        String getName();

        String getType();

        boolean isRevoked();

        Collection<Guild.Integration> getIntegrations();

        boolean isVerified();

        boolean hasFriendSync();

        boolean showActivities();

        Visibility getVisibility();

        enum Visibility {
            NONE(0),

            EVERYONE(1);

            public final int value;

            Visibility(int value) {
                this.value = value;
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
    }
}

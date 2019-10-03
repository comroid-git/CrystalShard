package de.kaleidox.crystalshard.api.entity.emoji;

// https://discordapp.com/developers/docs/resources/emoji#emoji-object-emoji-structure

import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

import de.kaleidox.crystalshard.adapter.Adapter;
import de.kaleidox.crystalshard.api.entity.Snowflake;
import de.kaleidox.crystalshard.api.entity.guild.Guild;
import de.kaleidox.crystalshard.api.entity.guild.Role;
import de.kaleidox.crystalshard.api.entity.user.User;
import de.kaleidox.crystalshard.api.model.Mentionable;
import de.kaleidox.crystalshard.core.api.cache.Cacheable;
import de.kaleidox.crystalshard.core.api.rest.DiscordEndpoint;
import de.kaleidox.crystalshard.core.api.rest.HTTPStatusCodes;
import de.kaleidox.crystalshard.core.api.rest.RestMethod;
import de.kaleidox.crystalshard.util.annotation.IntroducedBy;

import static de.kaleidox.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;
import static de.kaleidox.crystalshard.util.annotation.IntroducedBy.ImplementationSource.GETTER;
import static de.kaleidox.crystalshard.util.annotation.IntroducedBy.ImplementationSource.PRODUCTION;

public interface CustomEmoji extends Emoji, Mentionable, Snowflake, Cacheable {
    @IntroducedBy(PRODUCTION)
    Guild getGuild();

    @IntroducedBy(GETTER)
    CompletableFuture<Collection<Role>> requestWhitelistedRoles();

    @IntroducedBy(GETTER)
    CompletableFuture<User> requestCreator();

    @IntroducedBy(GETTER)
    CompletableFuture<Boolean> requestRequiresColons();

    @IntroducedBy(GETTER)
    CompletableFuture<Boolean> requestIsManaged();

    @IntroducedBy(GETTER)
    CompletableFuture<Boolean> requestIsAnimated();

    @Override
    default String getDiscordPrintableString() {
        return getMentionTag();
    }

    Updater createUpdater();

    @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/emoji#delete-guild-emoji")
    default CompletableFuture<Void> delete() {
        return Adapter.<Void>request(getAPI())
                .endpoint(DiscordEndpoint.CUSTOM_EMOJI_SPECIFIC, getGuild().getID(), getID())
                .method(RestMethod.DELETE)
                .expectCode(HTTPStatusCodes.NO_CONTENT)
                .executeAs(data -> getAPI().getCacheManager()
                        .delete(CustomEmoji.class, getID()));
    }

    static Builder builder(Guild guild) {
        return Adapter.create(Builder.class);
    }

    @IntroducedBy(PRODUCTION)
    interface Builder {
        Guild getGuild();

        Optional<String> getName();

        Builder setName(String name);

        Optional<InputStream> getEmojiInputStream();

        Builder setImage(InputStream inputStream);

        Builder setImage(URL url);

        Collection<Role> getWhitelistedRoles();

        Builder addWhitelistedRole(Role role);

        Builder removeWhitelistedRoleIf(Predicate<Role> tester);

        @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/emoji#create-guild-emoji")
        CompletableFuture<CustomEmoji> build();
    }

    @IntroducedBy(PRODUCTION)
    interface Updater {
        Guild getGuild();

        String getName();

        Updater setName(String name);

        Collection<Role> getWhitelistedRoles();

        Updater addWhitelistedRole(Role role);

        Updater removeWhitelistedRoleIf(Predicate<Role> tester);

        @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/emoji#modify-guild-emoji")
        CompletableFuture<CustomEmoji> update();
    }
}

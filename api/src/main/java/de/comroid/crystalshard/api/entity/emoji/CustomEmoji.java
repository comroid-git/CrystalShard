package de.comroid.crystalshard.api.entity.emoji;

// https://discordapp.com/developers/docs/resources/emoji#emoji-object-emoji-structure

import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

import de.comroid.crystalshard.adapter.Adapter;
import de.comroid.crystalshard.api.entity.Snowflake;
import de.comroid.crystalshard.api.entity.guild.Guild;
import de.comroid.crystalshard.api.entity.guild.Role;
import de.comroid.crystalshard.api.entity.user.User;
import de.comroid.crystalshard.api.model.Mentionable;
import de.comroid.crystalshard.core.api.cache.Cacheable;
import de.comroid.crystalshard.core.api.rest.DiscordEndpoint;
import de.comroid.crystalshard.core.api.rest.HTTPStatusCodes;
import de.comroid.crystalshard.core.api.rest.RestMethod;
import de.comroid.crystalshard.util.annotation.IntroducedBy;
import de.comroid.crystalshard.util.model.serialization.JsonBinding;
import de.comroid.crystalshard.util.model.serialization.JsonTraits;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import static de.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;
import static de.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.PRODUCTION;
import static de.comroid.crystalshard.util.model.serialization.JsonBinding.identity;
import static de.comroid.crystalshard.util.model.serialization.JsonBinding.underlying;
import static de.comroid.crystalshard.util.model.serialization.JsonBinding.underlyingCollective;

@JsonTraits(CustomEmoji.Trait.class)
public interface CustomEmoji extends Emoji, Mentionable, Snowflake, Cacheable {
    @IntroducedBy(PRODUCTION)
    Guild getGuild();

    default String getName() {
        return getTraitValue(Trait.NAME);
    }

    default Collection<Role> getRoles() {
        return getTraitValue(Trait.WHITELISTED_ROLES);
    }

    default Optional<User> getCreator() {
        return wrapTraitValue(Trait.CREATOR);
    }

    default Optional<Boolean> requiresColons() {
        return wrapTraitValue(Trait.REQUIRE_COLONS);
    }

    default Optional<Boolean> isManaged() {
        return wrapTraitValue(Trait.MANAGED);
    }

    default Optional<Boolean> isAnimated() {
        return wrapTraitValue(Trait.ANIMATED);
    }

    interface Trait extends Snowflake.Trait {
        JsonBinding<String, String> NAME = identity(JsonNode::asText, "name");
        JsonBinding<ArrayNode, Collection<Role>> WHITELISTED_ROLES = underlyingCollective("roles", Role.class, (api, data) -> api.getCacheManager()
                .getRoleByID(data.asLong())
                .orElseThrow());
        JsonBinding<JsonNode, User> CREATOR = underlying("user", User.class);
        JsonBinding<Boolean, Boolean> REQUIRE_COLONS = identity(JsonNode::asBoolean, "require_colons");
        JsonBinding<Boolean, Boolean> MANAGED = identity(JsonNode::asBoolean, "managed");
        JsonBinding<Boolean, Boolean> ANIMATED = identity(JsonNode::asBoolean, "animated");
    }
    
    CompletableFuture<CustomEmoji> requestMetadata();

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

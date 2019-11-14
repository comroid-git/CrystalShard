package de.comroid.crystalshard.api.entity.emoji;

// https://discordapp.com/developers/docs/resources/emoji#emoji-object-emoji-structure

import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

import de.comroid.crystalshard.adapter.Adapter;
import de.comroid.crystalshard.adapter.MainAPI;
import de.comroid.crystalshard.api.entity.Snowflake;
import de.comroid.crystalshard.api.entity.guild.Guild;
import de.comroid.crystalshard.api.entity.guild.Role;
import de.comroid.crystalshard.api.entity.user.User;
import de.comroid.crystalshard.api.model.Mentionable;
import de.comroid.crystalshard.core.cache.Cacheable;
import de.comroid.crystalshard.core.rest.DiscordEndpoint;
import de.comroid.crystalshard.core.rest.HTTPStatusCodes;
import de.comroid.crystalshard.core.rest.RestMethod;
import de.comroid.crystalshard.util.annotation.IntroducedBy;
import de.comroid.crystalshard.util.model.serialization.JSONBinding;
import de.comroid.crystalshard.util.model.serialization.JSONBindingLocation;

import com.alibaba.fastjson.JSONObject;

import static de.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;
import static de.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.PRODUCTION;
import static de.comroid.crystalshard.util.model.serialization.JSONBinding.identity;
import static de.comroid.crystalshard.util.model.serialization.JSONBinding.mappingCollection;
import static de.comroid.crystalshard.util.model.serialization.JSONBinding.require;

@MainAPI
@JSONBindingLocation(CustomEmoji.JSON.class)
public interface CustomEmoji extends Emoji, Mentionable, Snowflake, Cacheable {
    @IntroducedBy(PRODUCTION)
    Guild getGuild();

    default String getName() {
        return getBindingValue(JSON.NAME);
    }

    default Collection<Role> getRoles() {
        return getBindingValue(JSON.WHITELISTED_ROLES);
    }

    default Optional<User> getCreator() {
        return wrapBindingValue(JSON.CREATOR);
    }

    default Optional<Boolean> requiresColons() {
        return wrapBindingValue(JSON.REQUIRE_COLONS);
    }

    default Optional<Boolean> isManaged() {
        return wrapBindingValue(JSON.MANAGED);
    }

    default Optional<Boolean> isAnimated() {
        return wrapBindingValue(JSON.ANIMATED);
    }

    interface JSON extends Snowflake.JSON {
        JSONBinding.OneStage<String> NAME = identity("name", JSONObject::getString);
        JSONBinding.TriStage<Long, Role> WHITELISTED_ROLES = mappingCollection("roles", JSONObject::getLong, (api, id) -> api.getCacheManager()
                .getByID(Role.class, id)
                .orElseThrow());
        JSONBinding.TwoStage<JSONObject, User> CREATOR = require("user", User.class);
        JSONBinding.OneStage<Boolean> REQUIRE_COLONS = identity("require_colons", JSONObject::getBoolean);
        JSONBinding.OneStage<Boolean> MANAGED = identity("managed", JSONObject::getBoolean);
        JSONBinding.OneStage<Boolean> ANIMATED = identity("animated", JSONObject::getBoolean);
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
                .executeAsObject(data -> getAPI().getCacheManager()
                        .delete(CustomEmoji.class, getID()));
    }

    static Builder builder(Guild guild) {
        return Adapter.require(Builder.class);
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

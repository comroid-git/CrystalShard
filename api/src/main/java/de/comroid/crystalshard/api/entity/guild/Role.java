package de.comroid.crystalshard.api.entity.guild;

import java.awt.Color;
import java.util.Comparator;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.concurrent.CompletableFuture;

import de.comroid.crystalshard.adapter.Adapter;
import de.comroid.crystalshard.adapter.MainAPI;
import de.comroid.crystalshard.api.entity.Snowflake;
import de.comroid.crystalshard.api.event.role.RoleEvent;
import de.comroid.crystalshard.api.listener.ListenerSpec;
import de.comroid.crystalshard.api.listener.model.ListenerAttachable;
import de.comroid.crystalshard.api.model.Mentionable;
import de.comroid.crystalshard.api.model.permission.PermissionOverridable;
import de.comroid.crystalshard.api.model.permission.PermissionOverride;
import de.comroid.crystalshard.core.cache.Cacheable;
import de.comroid.crystalshard.core.rest.DiscordEndpoint;
import de.comroid.crystalshard.core.rest.HTTPStatusCodes;
import de.comroid.crystalshard.core.rest.RestMethod;
import de.comroid.crystalshard.util.annotation.IntroducedBy;
import de.comroid.crystalshard.util.model.serialization.JSONBinding;
import de.comroid.crystalshard.util.model.serialization.JSONBindingLocation;

import com.alibaba.fastjson.JSONObject;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import static de.comroid.crystalshard.core.cache.Cacheable.makeSubcacheableInfo;
import static de.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;
import static de.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.GETTER;
import static de.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.PRODUCTION;
import static de.comroid.crystalshard.util.model.serialization.JSONBinding.identity;
import static de.comroid.crystalshard.util.model.serialization.JSONBinding.simple;

@MainAPI
@JSONBindingLocation(Role.Trait.class)
@IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/topics/permissions#role-object")
public interface Role extends Snowflake, PermissionOverridable, Mentionable, Cacheable, ListenerAttachable<ListenerSpec.AttachableTo.Role<? extends RoleEvent>> {
    Comparator<Role> ROLE_COMPARATOR = Comparator.comparingInt(de.comroid.crystalshard.api.entity.guild.Role::getPosition);

    @CacheInformation.Marker
    CacheInformation<Guild> CACHE_INFORMATION = makeSubcacheableInfo(Guild.class, de.comroid.crystalshard.api.entity.guild.Role::getGuild);

    @Override
    @Contract(pure = true)
    default int compareTo(@NotNull Snowflake other) {
        if (other instanceof Role)
            return ROLE_COMPARATOR.compare(this, (Role) other);

        return ((Snowflake) this).compareTo(other);
    }

    @IntroducedBy(PRODUCTION)
    Guild getGuild();

    @IntroducedBy(GETTER)
    default String getName() {
        return getBindingValue(JSON.NAME);
    }

    @IntroducedBy(GETTER)
    default Optional<Color> getColor() {
        return wrapBindingValue(JSON.COLOR);
    }

    @IntroducedBy(GETTER)
    default boolean isHoisted() {
        return getBindingValue(JSON.HOIST);
    }

    @IntroducedBy(GETTER)
    default int getPosition() {
        return getBindingValue(JSON.POSITION);
    }

    @IntroducedBy(GETTER)
    default PermissionOverride getPermissions() {
        return getBindingValue(JSON.PERMISSIONS);
    }

    @IntroducedBy(GETTER)
    default boolean isManaged() {
        return getBindingValue(JSON.MANAGED);
    }

    @IntroducedBy(GETTER)
    default boolean isMentionable() {
        return getBindingValue(JSON.MENTIONABLE);
    }

    @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/guild#delete-guild-role")
    default CompletableFuture<Void> delete() {
        return Adapter.<Void>request(getAPI())
                .endpoint(DiscordEndpoint.ROLE, getGuild().getID(), getID())
                .method(RestMethod.DELETE)
                .expectCode(HTTPStatusCodes.NO_CONTENT)
                .executeAsObject(data -> getAPI().getCacheManager()
                        .deleteMember(Guild.class, Role.class, getGuild().getID(), getID()));
    }

    interface JSON extends Snowflake.Trait {
        JSONBinding.OneStage<String> NAME = identity("name", JSONObject::getString);
        JSONBinding.TwoStage<Integer, Color> COLOR = simple("color", JSONObject::getInteger, Color::new);
        JSONBinding.OneStage<Boolean> HOIST = identity("hoist", JSONObject::getBoolean);
        JSONBinding.OneStage<Integer> POSITION = identity("position", JSONObject::getInteger);
        JSONBinding.TwoStage<Integer, PermissionOverride> PERMISSIONS = simple("permissions", JSONObject::getInteger, PermissionOverride::fromBitmask);
        JSONBinding.OneStage<Boolean> MANAGED = identity("managed", JSONObject::getBoolean);
        JSONBinding.OneStage<Boolean> MENTIONABLE = identity("mentionable", JSONObject::getBoolean);
    }

    interface Builder {
        Guild getGuild();

        Optional<String> getName();

        Builder setName(String name);

        Optional<PermissionOverride> getPermissionOverride();

        Builder setPermissionOverride(PermissionOverride permissionOverride);

        Optional<Color> getColor();

        Builder setColor(Color color);

        boolean isHoisted();

        Builder setHoisted(boolean hoisted);

        boolean isMentionable();

        Builder setMentionable(boolean mentionable);

        OptionalInt getPosition();

        @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/guild#modify-guild-role-positions")
        Updater setPosition(int position);

        @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/guild#create-guild-role")
        CompletableFuture<Role> build();
    }

    interface Updater {
        Guild getGuild();

        String getName();

        Updater setName(String name);

        PermissionOverride getPermissionOverride();

        Updater setPermissionOverride(PermissionOverride permissionOverride);

        Optional<Color> getColor();

        Updater setColor(Color color);

        boolean isHoisted();

        Updater setHoisted(boolean hoisted);

        boolean isMentionable();

        Updater setMentionable(boolean mentionable);

        int getPosition();

        @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/guild#modify-guild-role-positions")
        Updater setPosition(int position);

        @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/guild#modify-guild-role")
        CompletableFuture<Role> update();
    }
}

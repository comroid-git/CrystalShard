package de.comroid.crystalshard.api.entity.guild;

import java.awt.Color;
import java.util.Comparator;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.concurrent.CompletableFuture;

import de.comroid.crystalshard.adapter.Adapter;
import de.comroid.crystalshard.api.entity.Snowflake;
import de.comroid.crystalshard.api.event.role.RoleEvent;
import de.comroid.crystalshard.api.listener.model.ListenerAttachable;
import de.comroid.crystalshard.api.listener.role.RoleAttachableListener;
import de.comroid.crystalshard.api.model.Mentionable;
import de.comroid.crystalshard.api.model.permission.PermissionOverridable;
import de.comroid.crystalshard.api.model.permission.PermissionOverride;
import de.comroid.crystalshard.core.api.cache.Cacheable;
import de.comroid.crystalshard.core.api.rest.DiscordEndpoint;
import de.comroid.crystalshard.core.api.rest.HTTPStatusCodes;
import de.comroid.crystalshard.core.api.rest.RestMethod;
import de.comroid.crystalshard.util.annotation.IntroducedBy;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import static de.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;
import static de.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.GETTER;
import static de.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.PRODUCTION;

@IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/topics/permissions#role-object")
public interface Role extends Snowflake, PermissionOverridable, Mentionable, Cacheable, ListenerAttachable<RoleAttachableListener<? extends RoleEvent>> {
    Comparator<Role> ROLE_COMPARATOR = Comparator.comparingInt(Role::getPosition);

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
    String getName();

    @IntroducedBy(GETTER)
    Optional<Color> getColor();

    @IntroducedBy(GETTER)
    boolean isHoisted();

    @IntroducedBy(GETTER)
    int getPosition();

    @IntroducedBy(GETTER)
    PermissionOverride getPermissions();

    @IntroducedBy(GETTER)
    boolean isManaged();

    @IntroducedBy(GETTER)
    boolean isMentionable();

    @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/guild#delete-guild-role")
    default CompletableFuture<Void> delete() {
        return Adapter.<Void>request(getAPI())
                .endpoint(DiscordEndpoint.ROLE, getGuild().getID(), getID())
                .method(RestMethod.DELETE)
                .expectCode(HTTPStatusCodes.NO_CONTENT)
                .executeAs(data -> getAPI().getCacheManager()
                        .deleteMember(Guild.class, Role.class, getGuild().getID(), getID()));
    }

    @Override
    default Optional<Long> getCacheParentID() {
        return OptionalLong.of(getGuild().getID());
    }

    @Override
    default Optional<Class<? extends Cacheable>> getCacheParentType() {
        return Optional.of(Guild.class);
    }

    @Override
    default boolean isSubcacheMember() {
        return true;
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

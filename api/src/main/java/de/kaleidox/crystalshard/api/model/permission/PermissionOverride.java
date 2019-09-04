package de.kaleidox.crystalshard.api.model.permission;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.kaleidox.crystalshard.adapter.Adapter;
import de.kaleidox.crystalshard.api.entity.channel.GuildChannel;

public interface PermissionOverride {
    Optional<? extends GuildChannel> getChannel();

    Optional<? extends PermissionOverridable> getTarget();

    State getState(Permission permission);

    default boolean isTargetUser() {
        return getTarget().flatMap(PermissionOverridable::asGuildMember).isPresent();
    }

    default boolean isTargetRole() {
        return getTarget().flatMap(PermissionOverridable::asRole).isPresent();
    }

    default Set<Permission> getAllowedPermissions() {
        return Stream.of(Permission.values())
                .filter(permission -> getState(permission) == State.ALLOWED)
                .collect(Collectors.toSet());
    }

    default Set<Permission> getDeniedPermissions() {
        return Stream.of(Permission.values())
                .filter(permission -> getState(permission) == State.DENIED)
                .collect(Collectors.toSet());
    }

    default Set<Permission> getUnsetPermissions() {
        return Stream.of(Permission.values())
                .filter(permission -> getState(permission) == State.UNSET)
                .collect(Collectors.toSet());
    }

    default boolean isAllowed(Permission permission) {
        return getState(permission) == State.ALLOWED;
    }

    default boolean isDenied(Permission permission) {
        return getState(permission) == State.DENIED;
    }

    default boolean isUnset(Permission permission) {
        return getState(permission) == State.UNSET;
    }

    static Builder builder() {
        return Adapter.create(Builder.class);
    }

    static PermissionOverride fromBitmask(int allowedBitmask) {
        return fromBitmasks(allowedBitmask, 0);
    }

    static PermissionOverride fromBitmasks(int allowedBitmask, int deniedBitmask) {
        Builder builder = builder();

        for (Permission permissionType : Permission.values()) {
            if (permissionType.isSet(allowedBitmask) && permissionType.isSet(deniedBitmask))
                builder.setState(permissionType, State.UNSET);
            else if (permissionType.isSet(allowedBitmask))
                builder.setState(permissionType, State.ALLOWED);
            else if (permissionType.isSet(deniedBitmask))
                builder.setState(permissionType, State.DENIED);
            else builder.setState(permissionType, State.UNSET);
        }

        return builder.build();
    }

    interface Builder {
        State getState(Permission permission);

        Builder setState(Permission permission, State state);

        PermissionOverride build();

        default Builder setAllowed(Permission... permissions) {
            for (Permission permission : permissions)
                setState(permission, State.ALLOWED);

            return this;
        }

        default Builder setDenied(Permission... permissions) {
            for (Permission permission : permissions)
                setState(permission, State.DENIED);

            return this;
        }

        default Builder unset(Permission... permissions) {
            for (Permission permission : permissions)
                setState(permission, State.UNSET);

            return this;
        }
    }

    enum State {
        ALLOWED,

        DENIED,

        UNSET
    }
}

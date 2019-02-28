package de.kaleidox.crystalshard.api.entity.server.permission;

import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.api.entity.server.Server;

import java.util.Map;
import java.util.stream.Stream;

public interface PermissionOverride extends Map<Permission, OverrideState> {
    Discord getDiscord();

    Server getServer();

    Type getOverrideType();

    PermissionOverwritable getParent();

    PermissionOverride addOverride(Permission permission, OverrideState state);

    PermissionOverride removeOverride(Permission permission);

    PermissionList getAllowed();

    PermissionList getDenied();

    int toPermissionInt();

    // Override Methods
    boolean equals(Object other);

    enum Type {
        UNKNOWN(""),
        ROLE("role"),
        USER("member");
        private final String key;

        Type(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }

        // Static members
        // Static membe
        public static Type getByKey(String key) {
            return Stream.of(values())
                    .filter(type -> type.key.equalsIgnoreCase(key))
                    .findAny()
                    .orElse(UNKNOWN);
        }
    }
}


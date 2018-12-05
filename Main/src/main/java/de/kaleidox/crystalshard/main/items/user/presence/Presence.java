package de.kaleidox.crystalshard.main.items.user.presence;

import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.items.user.ServerMember;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public interface Presence {
    ServerMember getUser();

    Optional<UserActivity> getActivity();

    Server getServer();

    Status getStatus();

    enum Status {
        UNKNOWN(null),
        HIDDEN("hidden"),
        OFFLINE("offline"),
        ONLINE("online"),
        DND("dnd"),
        IDLE("idle");
        private final String key;

        Status(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }

        // Static membe
        public static Status getFromKey(String key) {
            return Stream.of(values())
                    .filter(status -> Objects.nonNull(status.key))
                    .filter(status -> status.key.equalsIgnoreCase(key))
                    .findAny()
                    .orElse(UNKNOWN);
        }
    }
}

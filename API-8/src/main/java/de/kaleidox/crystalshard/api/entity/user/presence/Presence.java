package de.kaleidox.crystalshard.api.entity.user.presence;

import org.intellij.lang.annotations.MagicConstant;

import de.kaleidox.crystalshard.api.entity.server.Server;
import de.kaleidox.crystalshard.api.entity.user.ServerMember;

import java.util.Optional;

public interface Presence {
    ServerMember getUser();

    Optional<UserActivity> getActivity();

    Server getServer();

    @MagicConstant(flagsFromClass = Status.class)
    Status getStatus();

    class Status {
        public static final String HIDDEN = "hidden";
        public static final String OFFLINE = "offline";
        public static final String ONLINE = "online";
        public static final String DO_NOT_DISTURB = "dnd";
        public static final String IDLE = "idle";
    }
}

package de.comroid.crystalshard.api.event.user;

import java.util.Optional;

import de.comroid.crystalshard.api.entity.user.User;
import org.jetbrains.annotations.Nullable;

public interface WrappedUserEvent extends UserEvent {
    @Override
    default @Nullable User getTriggeringUser() {
        return wrapTriggeringUser().orElse(null);
    }

    Optional<User> wrapTriggeringUser();
}

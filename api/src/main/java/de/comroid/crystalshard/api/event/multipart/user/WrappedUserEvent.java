package de.comroid.crystalshard.api.event.multipart.user;

import java.util.Optional;

import de.comroid.crystalshard.api.entity.user.User;
import de.comroid.crystalshard.api.event.multipart.APIEvent;

import org.jetbrains.annotations.Nullable;

public interface WrappedUserEvent extends UserEvent, APIEvent {
    @Override
    default @Nullable User getUser() {
        return wrapUser().orElse(null);
    }

    Optional<User> wrapUser();
}

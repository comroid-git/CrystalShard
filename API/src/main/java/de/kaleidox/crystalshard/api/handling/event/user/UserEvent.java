package de.kaleidox.crystalshard.api.handling.event.user;

import de.kaleidox.crystalshard.api.entity.user.User;

public interface UserEvent {
    default long getUserId() {
        return getUser().getId();
    }

    User getUser();
}

package de.kaleidox.crystalshard.main.event;

import de.kaleidox.crystalshard.main.items.user.User;

public interface UserEvent {
    User getUser();

    default long getUserId() {
        return getUser().getId();
    }
}

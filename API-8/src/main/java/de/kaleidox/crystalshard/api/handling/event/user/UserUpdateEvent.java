package de.kaleidox.crystalshard.api.handling.event.user;

import de.kaleidox.crystalshard.api.handling.editevent.EditEvent;
import de.kaleidox.crystalshard.api.entity.user.User;

public interface UserUpdateEvent extends UserEvent, EditEvent<User> {
}

package de.kaleidox.crystalshard.main.handling.event.user;

import de.kaleidox.crystalshard.main.handling.editevent.EditEvent;
import de.kaleidox.crystalshard.main.items.user.User;

public interface UserUpdateEvent extends UserEvent, EditEvent<User> {
}

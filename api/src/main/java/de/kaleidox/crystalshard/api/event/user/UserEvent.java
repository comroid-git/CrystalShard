package de.kaleidox.crystalshard.api.event.user;

import de.kaleidox.crystalshard.api.entity.user.User;
import de.kaleidox.crystalshard.api.event.model.Event;

import org.jetbrains.annotations.NotNull;

public interface UserEvent extends Event {
    /*
    The @NotNull annotation being present and no #wrapUser method being present
    depends on whether there are any events that can be:
    - attached to a User
    - fired by something else than a User
     */
    @NotNull User getUser();
}

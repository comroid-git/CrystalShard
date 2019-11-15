package de.comroid.crystalshard.api.event.multipart.user;

import de.comroid.crystalshard.api.entity.user.User;
import de.comroid.crystalshard.api.event.multipart.APIEvent;

public interface UserEvent extends APIEvent {
    User getUser();
}

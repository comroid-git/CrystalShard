package org.comroid.crystalshard.api.event.multipart.user;

import org.comroid.crystalshard.api.entity.user.User;
import org.comroid.crystalshard.api.event.multipart.APIEvent;

public interface UserEvent extends APIEvent {
    User getUser();
}

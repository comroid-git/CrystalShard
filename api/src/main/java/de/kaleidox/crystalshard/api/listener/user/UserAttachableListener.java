package de.kaleidox.crystalshard.api.listener.user;

import de.kaleidox.crystalshard.api.event.user.UserEvent;
import de.kaleidox.crystalshard.api.listener.model.AttachableListener;
import de.kaleidox.crystalshard.api.listener.model.Listener;

public interface UserAttachableListener<E extends UserEvent> extends Listener<E>, AttachableListener {
}

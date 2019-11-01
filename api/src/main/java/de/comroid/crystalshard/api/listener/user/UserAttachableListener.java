package de.comroid.crystalshard.api.listener.user;

import de.comroid.crystalshard.api.event.user.UserEvent;
import de.comroid.crystalshard.api.listener.model.AttachableListener;
import de.comroid.crystalshard.api.listener.model.Listener;

public interface UserAttachableListener<E extends UserEvent> extends Listener<E>, AttachableListener {
}

package de.kaleidox.crystalshard.api.listener.role;

import de.kaleidox.crystalshard.api.event.role.RoleEvent;
import de.kaleidox.crystalshard.api.listener.model.AttachableListener;
import de.kaleidox.crystalshard.api.listener.model.Listener;

public interface RoleAttachableListener<E extends RoleEvent> extends Listener<E>, AttachableListener {
}

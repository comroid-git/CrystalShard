package de.comroid.crystalshard.api.listener.role;

import de.comroid.crystalshard.api.event.role.RoleEvent;
import de.comroid.crystalshard.api.listener.model.AttachableListener;
import de.comroid.crystalshard.api.listener.model.Listener;

public interface RoleAttachableListener<E extends RoleEvent> extends Listener<E>, AttachableListener {
}

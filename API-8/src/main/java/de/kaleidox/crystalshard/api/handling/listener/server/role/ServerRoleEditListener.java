package de.kaleidox.crystalshard.api.handling.listener.server.role;

import de.kaleidox.crystalshard.api.handling.event.server.role.RoleEditEvent;
import de.kaleidox.crystalshard.api.handling.listener.DiscordAttachableListener;
import de.kaleidox.crystalshard.api.handling.listener.server.ServerAttachableListener;

@FunctionalInterface
public interface ServerRoleEditListener extends DiscordAttachableListener, ServerAttachableListener, RoleAttachableListener {
    void onRoleEdit(RoleEditEvent event);
}

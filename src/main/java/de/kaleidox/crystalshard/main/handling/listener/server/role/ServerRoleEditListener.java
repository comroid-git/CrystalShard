package de.kaleidox.crystalshard.main.handling.listener.server.role;

import de.kaleidox.crystalshard.main.handling.event.server.role.RoleEditEvent;
import de.kaleidox.crystalshard.main.handling.listener.DiscordAttachableListener;
import de.kaleidox.crystalshard.main.handling.listener.server.ServerAttachableListener;

@FunctionalInterface
public interface ServerRoleEditListener extends DiscordAttachableListener, ServerAttachableListener,
        RoleAttachableListener {
    void onRoleEdit(RoleEditEvent event);
}

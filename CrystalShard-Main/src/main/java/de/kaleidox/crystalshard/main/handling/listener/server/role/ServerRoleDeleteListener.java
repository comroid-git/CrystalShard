package de.kaleidox.crystalshard.main.handling.listener.server.role;

import de.kaleidox.crystalshard.main.handling.event.server.role.RoleDeleteEvent;
import de.kaleidox.crystalshard.main.handling.listener.DiscordAttachableListener;
import de.kaleidox.crystalshard.main.handling.listener.server.ServerAttachableListener;

@FunctionalInterface
public interface ServerRoleDeleteListener extends DiscordAttachableListener, ServerAttachableListener, RoleAttachableListener {
    void onRoleDelete(RoleDeleteEvent event);
}

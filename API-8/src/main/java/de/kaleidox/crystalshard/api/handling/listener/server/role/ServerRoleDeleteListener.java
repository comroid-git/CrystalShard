package de.kaleidox.crystalshard.api.handling.listener.server.role;

import de.kaleidox.crystalshard.api.handling.event.server.role.RoleDeleteEvent;
import de.kaleidox.crystalshard.api.handling.listener.DiscordAttachableListener;
import de.kaleidox.crystalshard.api.handling.listener.server.ServerAttachableListener;

@FunctionalInterface
public interface ServerRoleDeleteListener extends DiscordAttachableListener, ServerAttachableListener, RoleAttachableListener {
    void onRoleDelete(RoleDeleteEvent event);
}

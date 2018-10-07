package de.kaleidox.crystalshard.main.handling.listener.server.role;

import de.kaleidox.crystalshard.main.handling.event.server.role.RoleCreateEvent;
import de.kaleidox.crystalshard.main.handling.listener.DiscordAttachableListener;
import de.kaleidox.crystalshard.main.handling.listener.server.ServerAttachableListener;

@FunctionalInterface
public interface ServerRoleCreateListener extends DiscordAttachableListener, ServerAttachableListener {
    void onRoleCreate(RoleCreateEvent event);
}

package de.kaleidox.crystalshard.api.handling.listener.server.role;

import de.kaleidox.crystalshard.api.handling.event.server.role.RoleCreateEvent;
import de.kaleidox.crystalshard.api.handling.listener.DiscordAttachableListener;
import de.kaleidox.crystalshard.api.handling.listener.server.ServerAttachableListener;

@FunctionalInterface
public interface ServerRoleCreateListener extends DiscordAttachableListener, ServerAttachableListener {
    void onRoleCreate(RoleCreateEvent event);
}

package de.kaleidox.crystalshard.main.handling.listener.server.other;

import de.kaleidox.crystalshard.main.handling.event.server.other.ServerPresenceUpdateEvent;
import de.kaleidox.crystalshard.main.handling.listener.DiscordAttachableListener;
import de.kaleidox.crystalshard.main.handling.listener.server.ServerAttachableListener;
import de.kaleidox.crystalshard.main.handling.listener.server.role.RoleAttachableListener;
import de.kaleidox.crystalshard.main.handling.listener.user.UserAttachableListener;

@FunctionalInterface
public interface ServerPresenceUpdateListener
        extends DiscordAttachableListener, ServerAttachableListener, RoleAttachableListener, UserAttachableListener {
    void onPresenceUpdate(ServerPresenceUpdateEvent event);
}

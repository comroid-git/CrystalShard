package de.kaleidox.crystalshard.main.handling.listener.server.member;

import de.kaleidox.crystalshard.main.handling.event.server.member.ServerMemberUpdateEvent;
import de.kaleidox.crystalshard.main.handling.listener.DiscordAttachableListener;
import de.kaleidox.crystalshard.main.handling.listener.server.ServerAttachableListener;
import de.kaleidox.crystalshard.main.handling.listener.user.UserAttachableListener;

@FunctionalInterface
public interface ServerMemberUpdateListener extends DiscordAttachableListener, ServerAttachableListener,
        UserAttachableListener {
    void onMemberUpdate(ServerMemberUpdateEvent event);
}

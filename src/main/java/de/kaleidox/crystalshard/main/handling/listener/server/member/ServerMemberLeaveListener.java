package de.kaleidox.crystalshard.main.handling.listener.server.member;

import de.kaleidox.crystalshard.main.handling.event.server.member.ServerMemberLeaveEvent;
import de.kaleidox.crystalshard.main.handling.listener.DiscordAttachableListener;
import de.kaleidox.crystalshard.main.handling.listener.server.ServerAttachableListener;
import de.kaleidox.crystalshard.main.handling.listener.user.UserAttachableListener;

@FunctionalInterface
public interface ServerMemberLeaveListener extends DiscordAttachableListener, ServerAttachableListener, UserAttachableListener {
    void onMemberLeave(ServerMemberLeaveEvent event);
}

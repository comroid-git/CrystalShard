package de.kaleidox.crystalshard.api.handling.listener.server.member;

import de.kaleidox.crystalshard.api.handling.event.server.member.ServerMemberLeaveEvent;
import de.kaleidox.crystalshard.api.handling.listener.DiscordAttachableListener;
import de.kaleidox.crystalshard.api.handling.listener.server.ServerAttachableListener;
import de.kaleidox.crystalshard.api.handling.listener.user.UserAttachableListener;

@FunctionalInterface
public interface ServerMemberLeaveListener extends DiscordAttachableListener, ServerAttachableListener, UserAttachableListener {
    void onMemberLeave(ServerMemberLeaveEvent event);
}

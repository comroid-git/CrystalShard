package de.kaleidox.crystalshard.api.handling.listener.server.member;

import de.kaleidox.crystalshard.api.handling.event.server.member.ServerMemberUpdateEvent;
import de.kaleidox.crystalshard.api.handling.listener.DiscordAttachableListener;
import de.kaleidox.crystalshard.api.handling.listener.server.ServerAttachableListener;
import de.kaleidox.crystalshard.api.handling.listener.user.UserAttachableListener;

@FunctionalInterface
public interface ServerMemberUpdateListener extends DiscordAttachableListener, ServerAttachableListener, UserAttachableListener {
    void onMemberUpdate(ServerMemberUpdateEvent event);
}

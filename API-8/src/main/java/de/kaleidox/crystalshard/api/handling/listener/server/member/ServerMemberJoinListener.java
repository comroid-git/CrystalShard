package de.kaleidox.crystalshard.api.handling.listener.server.member;

import de.kaleidox.crystalshard.api.handling.event.server.member.ServerMemberJoinEvent;
import de.kaleidox.crystalshard.api.handling.listener.DiscordAttachableListener;
import de.kaleidox.crystalshard.api.handling.listener.server.ServerAttachableListener;

@FunctionalInterface
public interface ServerMemberJoinListener extends DiscordAttachableListener, ServerAttachableListener {
    void onMemberJoin(ServerMemberJoinEvent event);
}

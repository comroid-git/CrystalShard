package de.kaleidox.crystalshard.main.handling.listener.server.member;

import de.kaleidox.crystalshard.main.handling.event.server.member.ServerMemberJoinEvent;
import de.kaleidox.crystalshard.main.handling.listener.DiscordAttachableListener;
import de.kaleidox.crystalshard.main.handling.listener.server.ServerAttachableListener;

@FunctionalInterface
public interface ServerMemberJoinListener extends DiscordAttachableListener, ServerAttachableListener {
    void onMemberJoin(ServerMemberJoinEvent event);
}

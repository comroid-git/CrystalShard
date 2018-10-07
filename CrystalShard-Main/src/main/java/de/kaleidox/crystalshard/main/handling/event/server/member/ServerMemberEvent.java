package de.kaleidox.crystalshard.main.handling.event.server.member;

import de.kaleidox.crystalshard.main.handling.event.server.ServerEvent;
import de.kaleidox.crystalshard.main.items.user.ServerMember;

public interface ServerMemberEvent extends ServerEvent {
    ServerMember getMember();
}

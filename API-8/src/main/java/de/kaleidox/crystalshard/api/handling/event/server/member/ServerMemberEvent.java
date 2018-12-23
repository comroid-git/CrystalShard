package de.kaleidox.crystalshard.api.handling.event.server.member;

import de.kaleidox.crystalshard.api.handling.event.server.ServerEvent;
import de.kaleidox.crystalshard.api.entity.user.ServerMember;

public interface ServerMemberEvent extends ServerEvent {
    ServerMember getMember();
}

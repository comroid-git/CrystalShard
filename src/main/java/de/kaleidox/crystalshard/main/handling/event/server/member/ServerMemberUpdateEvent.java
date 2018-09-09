package de.kaleidox.crystalshard.main.handling.event.server.member;

import de.kaleidox.crystalshard.main.handling.editevent.EditEvent;
import de.kaleidox.crystalshard.main.items.user.ServerMember;

public interface ServerMemberUpdateEvent extends ServerMemberEvent, EditEvent<ServerMember> {
}

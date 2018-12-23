package de.kaleidox.crystalshard.api.handling.event.server.member;

import de.kaleidox.crystalshard.api.handling.editevent.EditEvent;
import de.kaleidox.crystalshard.api.entity.role.Role;
import de.kaleidox.crystalshard.api.entity.user.ServerMember;

import java.util.List;
import java.util.Optional;

public interface ServerMemberUpdateEvent extends ServerMemberEvent, EditEvent<ServerMember> {
    List<Role> getMemberRoles();

    Optional<String> getNickname();
}

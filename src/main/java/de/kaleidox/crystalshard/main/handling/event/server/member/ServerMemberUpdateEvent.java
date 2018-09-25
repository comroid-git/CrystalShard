package de.kaleidox.crystalshard.main.handling.event.server.member;

import de.kaleidox.crystalshard.main.handling.editevent.EditEvent;
import de.kaleidox.crystalshard.main.items.role.Role;
import de.kaleidox.crystalshard.main.items.user.ServerMember;
import java.util.List;
import java.util.Optional;

public interface ServerMemberUpdateEvent extends ServerMemberEvent, EditEvent<ServerMember> {
    List<Role> getMemberRoles();
    
    Optional<String> getNickname();
}

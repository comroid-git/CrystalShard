package de.kaleidox.crystalshard.api.handling.editevent.enums;

import de.kaleidox.crystalshard.api.handling.editevent.EditTrait;
import de.kaleidox.crystalshard.api.entity.user.ServerMember;

public enum ServerMemberEditTrait implements EditTrait<ServerMember> {
    ROLES,
    USER,
    NICKNAME
}

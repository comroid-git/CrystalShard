package de.kaleidox.crystalshard.api.handling.editevent.enums;

import de.kaleidox.crystalshard.api.entity.user.ServerMember;
import de.kaleidox.crystalshard.api.handling.editevent.EditTrait;

public enum ServerMemberEditTrait implements EditTrait<ServerMember> {
    ROLES,
    USER,
    NICKNAME
}

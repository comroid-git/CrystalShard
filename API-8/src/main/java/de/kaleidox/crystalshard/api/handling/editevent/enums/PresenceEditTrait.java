package de.kaleidox.crystalshard.api.handling.editevent.enums;

import de.kaleidox.crystalshard.api.handling.editevent.EditTrait;
import de.kaleidox.crystalshard.api.entity.user.presence.Presence;

public enum PresenceEditTrait implements EditTrait<Presence> {
    ROLES,
    ACTIVITY,
    STATUS
}

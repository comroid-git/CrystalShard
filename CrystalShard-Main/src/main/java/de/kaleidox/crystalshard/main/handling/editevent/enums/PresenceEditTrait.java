package de.kaleidox.crystalshard.main.handling.editevent.enums;

import de.kaleidox.crystalshard.main.handling.editevent.EditTrait;
import de.kaleidox.crystalshard.main.items.user.presence.Presence;

public enum PresenceEditTrait implements EditTrait<Presence> {
    ROLES,
    ACTIVITY,
    STATUS
}

package de.kaleidox.crystalshard.api.handling.event.user;

import de.kaleidox.crystalshard.api.handling.editevent.EditEvent;
import de.kaleidox.crystalshard.api.entity.user.presence.Presence;

public interface PresenceUpdateEvent extends UserEvent, EditEvent<Presence> {
}

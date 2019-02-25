package de.kaleidox.crystalshard.api.handling.event.user;

import de.kaleidox.crystalshard.api.entity.user.presence.Presence;
import de.kaleidox.crystalshard.api.handling.editevent.EditEvent;

public interface PresenceUpdateEvent extends UserEvent, EditEvent<Presence> {
}

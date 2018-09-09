package de.kaleidox.crystalshard.main.handling.event.user;

import de.kaleidox.crystalshard.main.handling.editevent.EditEvent;
import de.kaleidox.crystalshard.main.handling.event.user.UserEvent;
import de.kaleidox.crystalshard.main.items.user.presence.PresenceState;

public interface PresenceUpdateEvent extends UserEvent, EditEvent<PresenceState> {
}

package de.kaleidox.crystalshard.api.handling.event.server.generic;

import de.kaleidox.crystalshard.api.entity.server.Server;
import de.kaleidox.crystalshard.api.handling.editevent.EditEvent;
import de.kaleidox.crystalshard.api.handling.event.server.ServerEvent;

public interface ServerEditEvent extends ServerEvent, EditEvent<Server> {
}

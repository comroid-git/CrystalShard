package de.kaleidox.crystalshard.api.handling.event.server.generic;

import de.kaleidox.crystalshard.api.handling.editevent.EditEvent;
import de.kaleidox.crystalshard.api.handling.event.server.ServerEvent;
import de.kaleidox.crystalshard.api.entity.server.Server;

public interface ServerEditEvent extends ServerEvent, EditEvent<Server> {
}

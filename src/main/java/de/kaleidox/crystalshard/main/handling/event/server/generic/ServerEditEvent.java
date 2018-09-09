package de.kaleidox.crystalshard.main.handling.event.server.generic;

import de.kaleidox.crystalshard.main.handling.editevent.EditEvent;
import de.kaleidox.crystalshard.main.handling.event.server.ServerEvent;
import de.kaleidox.crystalshard.main.items.server.Server;

public interface ServerEditEvent extends ServerEvent, EditEvent<Server> {
}

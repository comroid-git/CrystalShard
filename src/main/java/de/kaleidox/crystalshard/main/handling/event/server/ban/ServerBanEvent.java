package de.kaleidox.crystalshard.main.handling.event.server.ban;

import de.kaleidox.crystalshard.main.handling.event.server.ServerEvent;
import de.kaleidox.crystalshard.main.items.server.interactive.Ban;

public interface ServerBanEvent extends ServerEvent {
    Ban getBan();
}

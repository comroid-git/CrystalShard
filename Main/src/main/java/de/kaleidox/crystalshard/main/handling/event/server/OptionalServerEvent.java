package de.kaleidox.crystalshard.main.handling.event.server;

import de.kaleidox.crystalshard.main.items.DiscordItem;
import de.kaleidox.crystalshard.main.items.server.Server;
import java.util.Optional;

public interface OptionalServerEvent {
    default Optional<Long> getServerId() {
        return getServer().map(DiscordItem::getId);
    }

    Optional<Server> getServer();
}

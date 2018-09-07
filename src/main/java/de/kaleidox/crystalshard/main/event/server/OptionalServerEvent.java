package de.kaleidox.crystalshard.main.event.server;

import de.kaleidox.crystalshard.main.items.DiscordItem;
import de.kaleidox.crystalshard.main.items.server.Server;

import java.util.Optional;

public interface OptionalServerEvent {
    Optional<Server> getServer();

    default Optional<Long> getServerId() {
        return getServer().map(DiscordItem::getId);
    }
}

package de.kaleidox.crystalshard.api.handling.event.server;

import de.kaleidox.crystalshard.api.entity.DiscordItem;
import de.kaleidox.crystalshard.api.entity.server.Server;

import java.util.Optional;

public interface OptionalServerEvent {
    default Optional<Long> getServerId() {
        return getServer().map(DiscordItem::getId);
    }

    Optional<Server> getServer();
}

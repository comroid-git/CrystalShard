package de.kaleidox.crystalshard.api.entity.channel;

import java.util.Optional;

import de.kaleidox.crystalshard.api.entity.server.Server;

public interface PrivateChannel extends Channel {
    @Override
    default Optional<Server> getServer() {
        return Optional.empty();
    }
}

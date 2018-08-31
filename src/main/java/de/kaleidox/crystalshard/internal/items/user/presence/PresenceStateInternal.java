package de.kaleidox.crystalshard.internal.items.user.presence;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.internal.items.user.presence.UserActivityInternal;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.items.role.Role;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.items.user.User;
import de.kaleidox.crystalshard.main.items.user.presence.PresenceState;
import de.kaleidox.crystalshard.main.items.user.presence.UserActivity;
import de.kaleidox.logging.Logger;
import de.kaleidox.logging.StaticException;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class PresenceStateInternal implements PresenceState {
    private final User user;
    private final Collection<Role> roles;
    private final UserActivity game;
    private final Server server;
    private final Status status;

    public PresenceStateInternal(Discord discord, Server server, JsonNode data) {
        this.user = User.of(discord, data.get("user").get("id").asLong()).exceptionally(Logger::get).join();
        this.roles = user.getRoles(server);
        this.game = data.has("game") ?
                new UserActivityInternal(data.get("game")) : null;
        this.server = Server.of(discord, data.get("guild_id").asLong()).join();
        this.status = Status.getFromKey(data.get("status").asText());
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public Collection<Role> getRoles() {
        return roles;
    }

    @Override
    public Optional<UserActivity> getActivity() {
        return Optional.ofNullable(game);
    }

    @Override
    public Server getServer() {
        return server;
    }

    @Override
    public Status getStatus() {
        return status;
    }
}

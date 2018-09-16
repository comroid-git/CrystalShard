package de.kaleidox.crystalshard.internal.handling.event.server.role;

import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.handling.event.EventBase;
import de.kaleidox.crystalshard.main.handling.event.server.role.RoleDeleteEvent;
import de.kaleidox.crystalshard.main.items.role.Role;
import de.kaleidox.crystalshard.main.items.server.Server;

import java.util.Optional;

public class RoleDeleteEventInternal extends EventBase implements RoleDeleteEvent {
    private final Role role;
    private final Server server;

    public RoleDeleteEventInternal(DiscordInternal discordInternal,
                                   Role role,
                                   Server server) {
        super(discordInternal);
        this.role = role;
        this.server = server;
    }

    @Override
    public Optional<Role> getRole() {
        return Optional.ofNullable(role);
    }

    @Override
    public Server getServer() {
        return server;
    }
}

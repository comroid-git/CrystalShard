package de.kaleidox.crystalshard.internal.handling.event.server.role;

import de.kaleidox.crystalshard.api.entity.role.Role;
import de.kaleidox.crystalshard.api.entity.server.Server;
import de.kaleidox.crystalshard.api.handling.event.server.role.RoleCreateEvent;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.handling.event.EventBase;

public class RoleCreateEventInternal extends EventBase implements RoleCreateEvent {
    private final Server server;
    private final Role role;

    public RoleCreateEventInternal(DiscordInternal discordInternal, Server server, Role role) {
        super(discordInternal);
        this.server = server;
        this.role = role;
    }

    // Override Methods
    @Override
    public Role getRole() {
        return role;
    }

    @Override
    public Server getServer() {
        return server;
    }
}

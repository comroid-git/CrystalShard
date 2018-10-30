package de.kaleidox.crystalshard.internal.handling.event.server.role;

import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.handling.event.EventBase;
import de.kaleidox.crystalshard.main.handling.event.server.role.RoleCreateEvent;
import de.kaleidox.crystalshard.main.items.role.Role;
import de.kaleidox.crystalshard.main.items.server.Server;

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

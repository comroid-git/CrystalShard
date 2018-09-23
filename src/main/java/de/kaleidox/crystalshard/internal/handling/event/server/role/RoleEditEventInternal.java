package de.kaleidox.crystalshard.internal.handling.event.server.role;

import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.handling.event.EventBase;
import de.kaleidox.crystalshard.main.handling.editevent.EditTrait;
import de.kaleidox.crystalshard.main.handling.event.server.role.RoleEditEvent;
import de.kaleidox.crystalshard.main.items.role.Role;
import de.kaleidox.crystalshard.main.items.server.Server;

import java.util.Set;

public class RoleEditEventInternal extends EventBase implements RoleEditEvent {
    private final Server               server;
    private final Role                 role;
    private final Set<EditTrait<Role>> traits;
    
    public RoleEditEventInternal(DiscordInternal discordInternal, Server server, Role role,
                                 Set<EditTrait<Role>> traits) {
        super(discordInternal);
        this.server = server;
        this.role = role;
        this.traits = traits;
    }
    
    // Override Methods
    @Override
    public Set<EditTrait<Role>> getEditTraits() {
        return traits;
    }
    
    @Override
    public Role getRole() {
        return role;
    }
    
    @Override
    public Server getServer() {
        return server;
    }
}

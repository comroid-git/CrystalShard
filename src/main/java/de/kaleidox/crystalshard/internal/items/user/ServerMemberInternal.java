package de.kaleidox.crystalshard.internal.items.user;

import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.items.user.ServerMember;

public class ServerMemberInternal extends UserInternal implements ServerMember {
    private final Server server;
    
    public ServerMemberInternal(UserInternal user, Server server) {
        super(user);
        this.server = server;
    }
    
// Override Methods
    @Override
    public Server getServer() {
        return server;
    }
}

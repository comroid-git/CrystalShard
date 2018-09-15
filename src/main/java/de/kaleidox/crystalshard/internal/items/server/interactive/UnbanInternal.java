package de.kaleidox.crystalshard.internal.items.server.interactive;

import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.items.user.ServerMember;

public class UnbanInternal implements Unban {
    private final Server server;
    private final ServerMember user;

    public UnbanInternal(Server server, ServerMember user) {
        this.server = server;
        this.user = user;
    }

    @Override
    public ServerMember getUser() {
        return user;
    }

    @Override
    public Server getServer() {
        return server;
    }
}

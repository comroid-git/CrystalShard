package de.kaleidox.crystalshard.internal.items.server.interactive;

import de.kaleidox.crystalshard.api.entity.server.Server;
import de.kaleidox.crystalshard.api.entity.server.interactive.Ban;
import de.kaleidox.crystalshard.api.entity.user.ServerMember;

public class BanInternal implements Ban {
    private final Server server;
    private final ServerMember user;

    public BanInternal(Server server, ServerMember user) {
        this.server = server;
        this.user = user;
    }

    // Override Methods
    @Override
    public ServerMember getUser() {
        return user;
    }

    @Override
    public Server getServer() {
        return server;
    }
}

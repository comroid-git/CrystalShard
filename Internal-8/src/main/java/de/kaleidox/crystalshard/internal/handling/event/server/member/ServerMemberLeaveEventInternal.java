package de.kaleidox.crystalshard.internal.handling.event.server.member;

import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.handling.event.EventBase;
import de.kaleidox.crystalshard.main.handling.event.server.member.ServerMemberLeaveEvent;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.items.user.User;

public class ServerMemberLeaveEventInternal extends EventBase implements ServerMemberLeaveEvent {
    private final Server server;
    private final User user;

    public ServerMemberLeaveEventInternal(DiscordInternal discordInternal, Server server, User user) {
        super(discordInternal);
        this.server = server;
        this.user = user;
    }

    // Override Methods
    @Override
    public Server getServer() {
        return server;
    }

    @Override
    public User getUser() {
        return user;
    }
}

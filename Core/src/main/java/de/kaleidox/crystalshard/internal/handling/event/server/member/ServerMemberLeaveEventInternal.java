package de.kaleidox.crystalshard.internal.handling.event.server.member;

import de.kaleidox.crystalshard.api.entity.server.Server;
import de.kaleidox.crystalshard.api.entity.user.User;
import de.kaleidox.crystalshard.api.handling.event.server.member.ServerMemberLeaveEvent;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.handling.event.EventBase;

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

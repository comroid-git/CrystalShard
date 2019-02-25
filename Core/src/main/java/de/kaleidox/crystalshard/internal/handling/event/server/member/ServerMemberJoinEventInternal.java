package de.kaleidox.crystalshard.internal.handling.event.server.member;

import de.kaleidox.crystalshard.api.entity.server.Server;
import de.kaleidox.crystalshard.api.entity.user.ServerMember;
import de.kaleidox.crystalshard.api.handling.event.server.member.ServerMemberJoinEvent;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.handling.event.EventBase;

public class ServerMemberJoinEventInternal extends EventBase implements ServerMemberJoinEvent {
    private final Server server;
    private final ServerMember member;

    public ServerMemberJoinEventInternal(DiscordInternal discordInternal, Server server, ServerMember member) {
        super(discordInternal);
        this.server = server;
        this.member = member;
    }

    // Override Methods
    @Override
    public ServerMember getMember() {
        return member;
    }

    @Override
    public Server getServer() {
        return server;
    }
}

package de.kaleidox.crystalshard.internal.handling.event.server.member;

import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.handling.event.EventBase;
import de.kaleidox.crystalshard.main.handling.event.server.member.ServerMemberJoinEvent;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.items.user.ServerMember;

public class ServerMemberJoinEventInternal extends EventBase implements ServerMemberJoinEvent {
    private final Server       server;
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

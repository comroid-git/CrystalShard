package de.kaleidox.crystalshard.internal.handling.event.server.generic;

import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.handling.event.EventBase;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.handling.event.server.generic.ServerCreateEvent;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.items.user.ServerMember;

public class ServerCreateEventInternal extends EventBase implements ServerCreateEvent {
    private final Server server;
    
    public ServerCreateEventInternal(DiscordInternal discordInternal, Server server) {
        super(discordInternal);
        this.server = server;
    }
    
    // Override Methods
    @Override
    public ServerMember getOwner() {
        return server.getOwner();
    }
    
    @Override
    public Server getServer() {
        return server;
    }
    
    @Override
    public Discord getDiscord() {
        return super.getDiscord();
    }
}

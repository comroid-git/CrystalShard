package de.kaleidox.crystalshard.internal.handling.event.server.generic;

import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.api.entity.server.Server;
import de.kaleidox.crystalshard.api.entity.user.ServerMember;
import de.kaleidox.crystalshard.api.handling.event.server.generic.ServerCreateEvent;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.handling.event.EventBase;

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

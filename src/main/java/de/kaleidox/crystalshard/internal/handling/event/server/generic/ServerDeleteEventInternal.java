package de.kaleidox.crystalshard.internal.handling.event.server.generic;

import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.handling.event.EventBase;
import de.kaleidox.crystalshard.internal.items.server.ServerInternal;
import de.kaleidox.crystalshard.main.handling.event.server.generic.ServerDeleteEvent;
import de.kaleidox.crystalshard.main.items.server.Server;

import java.util.Optional;

public class ServerDeleteEventInternal extends EventBase implements ServerDeleteEvent {
    private final boolean gotKicked;
    private final long serverId;

    public ServerDeleteEventInternal(DiscordInternal discordInternal, long serverId, boolean gotKicked) {
        super(discordInternal);
        this.serverId = serverId;
        this.gotKicked = gotKicked;
    }

    @Override
    public boolean gotKicked() {
        return gotKicked;
    }

    @Override
    public Optional<Server> getServer() {
        return ServerInternal.getInstance(serverId);
    }
}

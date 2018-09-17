package de.kaleidox.crystalshard.internal.handling.event.server.ban;

import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.handling.event.EventBase;
import de.kaleidox.crystalshard.main.handling.event.server.ban.ServerUnbanEvent;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.items.server.interactive.Ban;

public class ServerUnbanEventInternal extends EventBase implements ServerUnbanEvent {
    public ServerUnbanEventInternal(DiscordInternal discordInternal) {
        super(discordInternal);
    }

// Override Methods
    @Override
    public Ban getUnban() {
        return null;
    }

    @Override
    public Server getServer() {
        return null;
    }
}

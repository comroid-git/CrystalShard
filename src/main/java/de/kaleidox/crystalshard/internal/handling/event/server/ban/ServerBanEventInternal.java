package de.kaleidox.crystalshard.internal.handling.event.server.ban;

import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.handling.event.EventBase;
import de.kaleidox.crystalshard.main.handling.event.server.ban.ServerBanEvent;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.items.server.interactive.Ban;

public class ServerBanEventInternal extends EventBase implements ServerBanEvent {
    private final Ban ban;

    public ServerBanEventInternal(DiscordInternal discordInternal, Ban ban) {
        super(discordInternal);
        this.ban = ban;
    }

// Override Methods
    @Override
    public Ban getBan() {
        return ban;
    }

    @Override
    public Server getServer() {
        return ban.getServer();
    }
}

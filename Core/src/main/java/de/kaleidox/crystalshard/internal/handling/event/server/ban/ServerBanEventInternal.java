package de.kaleidox.crystalshard.internal.handling.event.server.ban;

import de.kaleidox.crystalshard.api.entity.server.Server;
import de.kaleidox.crystalshard.api.entity.server.interactive.Ban;
import de.kaleidox.crystalshard.api.handling.event.server.ban.ServerBanEvent;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.handling.event.EventBase;

public class ServerBanEventInternal extends EventBase implements ServerBanEvent {
    private final Ban ban;

    public ServerBanEventInternal(DiscordInternal discordInternal, Ban ban) {
        super(discordInternal);
        this.ban = ban;
    }

    @Override
    public Server getServer() {
        return ban.getServer();
    }

    // Override Methods
    @Override
    public Ban getBan() {
        return ban;
    }
}

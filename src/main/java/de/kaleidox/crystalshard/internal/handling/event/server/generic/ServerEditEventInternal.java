package de.kaleidox.crystalshard.internal.handling.event.server.generic;

import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.handling.event.EventBase;
import de.kaleidox.crystalshard.main.handling.editevent.EditTrait;
import de.kaleidox.crystalshard.main.handling.event.server.generic.ServerEditEvent;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.util.annotations.DontContainNull;
import de.kaleidox.util.annotations.NotNull;

import java.util.Set;

public class ServerEditEventInternal extends EventBase implements ServerEditEvent {
    private final Server server;
    private final Set<EditTrait<Server>> editTraits;

    public ServerEditEventInternal(DiscordInternal discordInternal,
                                   @NotNull Server server,
                                   @DontContainNull Set<EditTrait<Server>> editTraits) {
        super(discordInternal);
        this.server = server;
        this.editTraits = editTraits;
    }

    @Override
    public Set<EditTrait<Server>> getEditTraits() {
        return editTraits;
    }

    @Override
    public Server getServer() {
        return server;
    }
}

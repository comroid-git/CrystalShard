package de.kaleidox.crystalshard.internal.handling.event.server.other;

import de.kaleidox.crystalshard.api.entity.server.Server;
import de.kaleidox.crystalshard.api.entity.user.ServerMember;
import de.kaleidox.crystalshard.api.entity.user.presence.Presence;
import de.kaleidox.crystalshard.api.entity.user.presence.UserActivity;
import de.kaleidox.crystalshard.api.handling.event.server.other.ServerPresenceUpdateEvent;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.handling.event.EventBase;

import java.util.Optional;

public class ServerPresenceUpdateEventInternal extends EventBase implements ServerPresenceUpdateEvent {
    private final Presence presence;

    public ServerPresenceUpdateEventInternal(DiscordInternal discordInternal, Presence presence) {
        super(discordInternal);
        this.presence = presence;
    }

    @Override
    public Presence getPresence() {
        return presence;
    }

    @Override
    public Optional<UserActivity> getActivity() {
        return presence.getActivity();
    }

    @Override
    public String getStatus() {
        return presence.getStatus();
    }

    @Override
    public ServerMember getMember() {
        return presence.getUser();
    }

    @Override
    public Server getServer() {
        return presence.getServer();
    }
}

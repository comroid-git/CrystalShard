package de.kaleidox.crystalshard.internal.handling.event.server.other;

import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.handling.event.EventBase;
import de.kaleidox.crystalshard.main.handling.event.server.other.ServerPresenceUpdateEvent;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.items.user.ServerMember;
import de.kaleidox.crystalshard.main.items.user.presence.Presence;
import de.kaleidox.crystalshard.main.items.user.presence.UserActivity;

import java.util.Optional;

public class ServerPresenceUpdateEventInternal extends EventBase implements ServerPresenceUpdateEvent {
    private final Presence presence;
    
    public ServerPresenceUpdateEventInternal(DiscordInternal discordInternal, Presence presence) {
        super(discordInternal);
        this.presence = presence;
    }
    
    // Override Methods
    @Override
    public Presence getPresence() {
        return presence;
    }
    
    @Override
    public Optional<UserActivity> getActivity() {
        return presence.getActivity();
    }
    
    @Override
    public Presence.Status getStatus() {
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

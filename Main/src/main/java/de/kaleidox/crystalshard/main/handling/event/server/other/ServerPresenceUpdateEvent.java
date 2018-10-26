package de.kaleidox.crystalshard.main.handling.event.server.other;

import de.kaleidox.crystalshard.main.handling.event.server.ServerEvent;
import de.kaleidox.crystalshard.main.handling.event.server.member.ServerMemberEvent;
import de.kaleidox.crystalshard.main.items.user.presence.Presence;
import de.kaleidox.crystalshard.main.items.user.presence.UserActivity;

import java.util.Optional;

public interface ServerPresenceUpdateEvent extends ServerEvent, ServerMemberEvent {
    Presence getPresence();
    
    Optional<UserActivity> getActivity();
    
    Presence.Status getStatus();
}

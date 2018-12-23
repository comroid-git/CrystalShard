package de.kaleidox.crystalshard.api.handling.event.server.other;

import de.kaleidox.crystalshard.api.handling.event.server.ServerEvent;
import de.kaleidox.crystalshard.api.handling.event.server.member.ServerMemberEvent;
import de.kaleidox.crystalshard.api.entity.user.presence.Presence;
import de.kaleidox.crystalshard.api.entity.user.presence.UserActivity;

import java.util.Optional;

public interface ServerPresenceUpdateEvent extends ServerEvent, ServerMemberEvent {
    Presence getPresence();

    Optional<UserActivity> getActivity();

    Presence.Status getStatus();
}

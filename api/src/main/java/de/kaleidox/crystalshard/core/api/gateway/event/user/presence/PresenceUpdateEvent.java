package de.kaleidox.crystalshard.core.api.gateway.event.user.presence;

// https://discordapp.com/developers/docs/topics/gateway#presence-update

import java.util.Collection;
import java.util.Optional;

import de.kaleidox.crystalshard.api.entity.guild.Guild;
import de.kaleidox.crystalshard.api.entity.guild.Role;
import de.kaleidox.crystalshard.api.entity.user.User;
import de.kaleidox.crystalshard.api.model.user.Presence;
import de.kaleidox.crystalshard.core.api.gateway.event.GatewayEvent;
import de.kaleidox.crystalshard.core.api.gateway.listener.user.presence.PresenceUpdateListener;
import de.kaleidox.crystalshard.util.annotation.ManagedBy;

@ManagedBy(PresenceUpdateListener.Manager.class)
public interface PresenceUpdateEvent extends GatewayEvent {
    String NAME = "PRESENCE_UPDATE_EVENT";

    User getUser();

    Collection<Role> getRoles();

    Optional<Presence.Activity> getActivity();

    Guild getGuild();

    Presence.Status getStatus();

    Collection<Presence.Activity> getActivities();

    Presence.ClientStatus getClientStatus();
}

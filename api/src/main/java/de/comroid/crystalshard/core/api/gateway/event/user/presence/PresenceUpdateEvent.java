package de.comroid.crystalshard.core.api.gateway.event.user.presence;

// https://discordapp.com/developers/docs/topics/gateway#presence-update

import java.util.Collection;
import java.util.Optional;

import de.comroid.crystalshard.api.entity.guild.Guild;
import de.comroid.crystalshard.api.entity.guild.Role;
import de.comroid.crystalshard.api.entity.user.User;
import de.comroid.crystalshard.api.model.user.Presence;
import de.comroid.crystalshard.core.api.gateway.event.GatewayEvent;
import de.comroid.crystalshard.core.api.gateway.listener.user.presence.PresenceUpdateListener;
import de.comroid.crystalshard.util.annotation.InitializedBy;

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

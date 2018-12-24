package de.kaleidox.crystalshard.internal.handling.handlers;

import com.fasterxml.jackson.databind.JsonNode;

import de.kaleidox.crystalshard.api.entity.role.Role;
import de.kaleidox.crystalshard.api.entity.server.Server;
import de.kaleidox.crystalshard.api.entity.user.ServerMember;
import de.kaleidox.crystalshard.api.entity.user.presence.Presence;
import de.kaleidox.crystalshard.api.handling.listener.server.other.ServerPresenceUpdateListener;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.handling.event.server.other.ServerPresenceUpdateEventInternal;
import de.kaleidox.crystalshard.internal.items.user.presence.PresenceInternal;
import de.kaleidox.crystalshard.internal.util.RoleContainer;

import java.util.Collection;
import java.util.Collections;

public class PRESENCE_UPDATE extends HandlerBase {
    // Override Methods
    @Override
    public void handle(DiscordInternal discord, JsonNode data) {
        Presence presence = PresenceInternal.getInstance(discord, data);
        Server server = presence.getServer();
        ServerMember user = presence.getUser();
        Collection<Role> roles = (user != null ? user.getRoles(server) : Collections.emptyList());

        ServerPresenceUpdateEventInternal event = new ServerPresenceUpdateEventInternal(discord, presence);

        collectListeners(ServerPresenceUpdateListener.class, discord, server, user, new RoleContainer(roles)).forEach(listener -> discord.getThreadPool()
                .execute(() -> listener.onPresenceUpdate(event)));
    }
}

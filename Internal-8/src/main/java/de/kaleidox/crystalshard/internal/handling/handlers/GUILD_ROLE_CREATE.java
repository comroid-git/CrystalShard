package de.kaleidox.crystalshard.internal.handling.handlers;

import com.fasterxml.jackson.databind.JsonNode;

import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.handling.event.server.role.RoleCreateEventInternal;
import de.kaleidox.crystalshard.internal.items.server.ServerInternal;
import de.kaleidox.crystalshard.main.handling.listener.server.role.ServerRoleCreateListener;
import de.kaleidox.crystalshard.main.items.role.Role;

public class GUILD_ROLE_CREATE extends HandlerBase {
    // Override Methods
    @Override
    public void handle(DiscordInternal discord, JsonNode data) {
        long serverId = data.get("guild_id")
                .asLong();
        ServerInternal server = (ServerInternal) discord.getServerCache()
                .getOrRequest(serverId, serverId);
        Role role = discord.getRoleCache()
                .getOrCreate(discord, server, data.get("role"));

        server.addRole(role);
        RoleCreateEventInternal event = new RoleCreateEventInternal(discord, server, role);

        collectListeners(ServerRoleCreateListener.class, discord, server).forEach(listener -> discord.getThreadPool()
                .execute(() -> listener.onRoleCreate(event)));
    }
}

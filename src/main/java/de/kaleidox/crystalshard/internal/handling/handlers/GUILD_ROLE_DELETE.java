package de.kaleidox.crystalshard.internal.handling.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.handling.event.server.role.RoleDeleteEventInternal;
import de.kaleidox.crystalshard.internal.items.role.RoleInternal;
import de.kaleidox.crystalshard.internal.items.server.ServerInternal;
import de.kaleidox.crystalshard.main.handling.listener.server.role.ServerRoleDeleteListener;

public class GUILD_ROLE_DELETE extends HandlerBase {
    @Override
    public void handle(DiscordInternal discord, JsonNode data) {
        ServerInternal server = (ServerInternal) ServerInternal.getInstance(discord, data.get("guild_id").asLong());
        RoleInternal role = (RoleInternal) RoleInternal.getInstance(server, data.get("role_id").asLong());

        server.removeRole(role);
        RoleDeleteEventInternal event = new RoleDeleteEventInternal(discord, role, server);

        collectListeners(ServerRoleDeleteListener.class, discord, server, role)
                .forEach(listener -> discord.getThreadPool()
                        .execute(() -> listener.onRoleDelete(event))
                );
    }
}

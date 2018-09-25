package de.kaleidox.crystalshard.internal.handling.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.handling.event.server.role.RoleEditEventInternal;
import de.kaleidox.crystalshard.internal.items.role.RoleInternal;
import de.kaleidox.crystalshard.internal.items.server.ServerInternal;
import de.kaleidox.crystalshard.main.handling.editevent.EditTrait;
import de.kaleidox.crystalshard.main.handling.listener.server.role.ServerRoleEditListener;
import de.kaleidox.crystalshard.main.items.role.Role;
import java.util.HashSet;
import java.util.Set;

public class GUILD_ROLE_UPDATE extends HandlerBase {
    // Override Methods
    @Override
    public void handle(DiscordInternal discord, JsonNode data) {
        long serverId = data.get("guild_id")
                .asLong();
        ServerInternal server = (ServerInternal) discord.getServerCache()
                .getOrRequest(serverId, serverId);
        RoleInternal role = (RoleInternal) discord.getRoleCache()
                .getOrCreate(discord, server, data);
        
        Set<EditTrait<Role>> traits = new HashSet<>(role.updateData(data.get("role")));
        RoleEditEventInternal event = new RoleEditEventInternal(discord, server, role, traits);
        
        collectListeners(ServerRoleEditListener.class,
                         discord,
                         server,
                         role).forEach(listener -> discord.getThreadPool()
                .execute(() -> listener.onRoleEdit(event)));
    }
}

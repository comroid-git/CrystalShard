package de.kaleidox.crystalshard.internal.handling.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.handling.event.server.member.ServerMemberUpdateEventInternal;
import de.kaleidox.crystalshard.main.handling.listener.server.member.ServerMemberUpdateListener;
import de.kaleidox.crystalshard.main.items.role.Role;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.items.user.ServerMember;
import de.kaleidox.util.objects.markers.IDPair;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GUILD_MEMBER_UPDATE extends HandlerBase {
    @Override
    public void handle(DiscordInternal discord, JsonNode data) {
        long serverId = data.get("guild_id")
                .asLong();
        Server server = discord.getServerCache()
                .getOrRequest(serverId, serverId);
        List<Role> userRoles = new ArrayList<>();
        data.get("roles")
                .forEach(roleId -> userRoles.add(discord.getRoleCache()
                        .getOrRequest(roleId.asLong(), IDPair.of(serverId, roleId.asLong()))));
        ServerMember user = discord.getUserCache()
                .getOrCreate(discord, data.get("user"))
                .toServerMember(server, data);
        String nickname = data.get("nick")
                .asText();

        ServerMemberUpdateEventInternal event = new ServerMemberUpdateEventInternal(discord, userRoles, nickname, Collections.emptySet(), user, server);

        collectListeners(ServerMemberUpdateListener.class, discord, server, user).forEach(listener -> discord.getThreadPool()
                .execute(() -> listener.onMemberUpdate(event)));
    }
}

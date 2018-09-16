package de.kaleidox.crystalshard.internal.handling.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.handling.event.server.member.ServerMemberUpdateEventInternal;
import de.kaleidox.crystalshard.internal.items.role.RoleInternal;
import de.kaleidox.crystalshard.internal.items.server.ServerInternal;
import de.kaleidox.crystalshard.internal.items.user.UserInternal;
import de.kaleidox.crystalshard.main.handling.listener.server.member.ServerMemberUpdateListener;
import de.kaleidox.crystalshard.main.items.role.Role;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.items.user.ServerMember;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GUILD_MEMBER_UPDATE extends HandlerBase {
    @Override
    public void handle(DiscordInternal discord, JsonNode data) {
        Server server = ServerInternal.getInstance(discord, data.get("guild_id").asLong());
        List<Role> userRoles = new ArrayList<>();
        data.get("roles").forEach(role_id -> userRoles.add(RoleInternal.getInstance(server, role_id.asLong())));
        ServerMember user = UserInternal.getInstance(discord, data.get("user")).toServerMember(server);
        String nickname = data.get("nick").asText();

        ServerMemberUpdateEventInternal event = new ServerMemberUpdateEventInternal(discord,
                userRoles,
                nickname,
                Collections.emptySet(),
                user,
                server);

        collectListeners(ServerMemberUpdateListener.class, discord, server, user)
                .forEach(listener -> discord.getThreadPool()
                        .execute(() -> listener.onMemberUpdate(event))
                );
    }
}

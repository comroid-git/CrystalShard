package de.kaleidox.crystalshard.internal.handling.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.handling.event.server.member.ServerMemberLeaveEventInternal;
import de.kaleidox.crystalshard.internal.items.server.ServerInternal;
import de.kaleidox.crystalshard.main.handling.listener.server.member.ServerMemberLeaveListener;
import de.kaleidox.crystalshard.main.items.user.User;

public class GUILD_MEMBER_REMOVE extends HandlerBase {
    // Override Methods
    @Override
    public void handle(DiscordInternal discord, JsonNode data) {
        long serverId = data.get("guild_id")
                .asLong();
        ServerInternal server = (ServerInternal) discord.getServerCache()
                .getOrRequest(serverId, serverId);
        User user = discord.getUserCache()
                .getOrCreate(discord, data);

        server.removeUser(user);
        ServerMemberLeaveEventInternal event = new ServerMemberLeaveEventInternal(discord, server, user);

        collectListeners(ServerMemberLeaveListener.class, discord, server, user).forEach(listener -> discord.getThreadPool()
                .execute(() -> listener.onMemberLeave(event)));
    }
}

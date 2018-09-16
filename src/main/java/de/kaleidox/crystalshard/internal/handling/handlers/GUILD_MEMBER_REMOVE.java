package de.kaleidox.crystalshard.internal.handling.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.handling.event.server.member.ServerMemberLeaveEventInternal;
import de.kaleidox.crystalshard.internal.items.server.ServerInternal;
import de.kaleidox.crystalshard.internal.items.user.UserInternal;
import de.kaleidox.crystalshard.main.handling.listener.server.member.ServerMemberLeaveListener;
import de.kaleidox.crystalshard.main.items.user.User;

public class GUILD_MEMBER_REMOVE extends HandlerBase {
    @Override
    public void handle(DiscordInternal discord, JsonNode data) {
        ServerInternal server = (ServerInternal) ServerInternal.getInstance(discord, data.get("guild_id").asLong());
        User user = UserInternal.getInstance(discord, data);

        server.removeUser(user);
        ServerMemberLeaveEventInternal event = new ServerMemberLeaveEventInternal(discord, server, user);

        collectListeners(ServerMemberLeaveListener.class, discord, server, user)
                .forEach(listener -> discord.getThreadPool()
                        .execute(() -> listener.onMemberLeave(event))
                );
    }
}

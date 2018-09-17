package de.kaleidox.crystalshard.internal.handling.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.handling.event.server.member.ServerMemberJoinEventInternal;
import de.kaleidox.crystalshard.internal.items.server.ServerInternal;
import de.kaleidox.crystalshard.internal.items.user.UserInternal;
import de.kaleidox.crystalshard.main.handling.listener.server.member.ServerMemberJoinListener;
import de.kaleidox.crystalshard.main.items.user.ServerMember;

public class GUILD_MEMBER_ADD extends HandlerBase {
// Override Methods
    @Override
    public void handle(DiscordInternal discord, JsonNode data) {
        ServerInternal server = (ServerInternal) ServerInternal.getInstance(discord, data.get("guild_id"));
        ServerMember member = UserInternal.getInstance(discord, data).toServerMember(server);
        
        server.addUser(member);
        ServerMemberJoinEventInternal event = new ServerMemberJoinEventInternal(discord, server, member);
        
        collectListeners(ServerMemberJoinListener.class, discord, server).forEach(listener -> discord.getThreadPool()
                .execute(() -> listener.onMemberJoin(event)));
    }
}

package de.kaleidox.crystalshard.internal.handling.handlers;

import com.fasterxml.jackson.databind.JsonNode;

import de.kaleidox.crystalshard.api.entity.user.ServerMember;
import de.kaleidox.crystalshard.api.handling.listener.server.member.ServerMemberJoinListener;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.handling.event.server.member.ServerMemberJoinEventInternal;
import de.kaleidox.crystalshard.internal.items.server.ServerInternal;

public class GUILD_MEMBER_ADD extends HandlerBase {
    // Override Methods
    @Override
    public void handle(DiscordInternal discord, JsonNode data) {
        long guildId = data.get("guild_id")
                .asLong();
        ServerInternal server = (ServerInternal) discord.getServerCache()
                .getOrRequest(guildId, guildId);
        ServerMember member = discord.getUserCache()
                .getOrCreate(discord, data)
                .toServerMember(server, data);

        server.addUser(member);
        ServerMemberJoinEventInternal event = new ServerMemberJoinEventInternal(discord, server, member);

        collectListeners(ServerMemberJoinListener.class, discord, server).forEach(listener -> discord.getThreadPool()
                .execute(() -> listener.onMemberJoin(event)));
    }
}

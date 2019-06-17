package de.kaleidox.crystalshard.internal.handling.handlers;

import com.fasterxml.jackson.databind.JsonNode;

import de.kaleidox.crystalshard.api.entity.server.Server;
import de.kaleidox.crystalshard.api.entity.server.interactive.Ban;
import de.kaleidox.crystalshard.api.entity.user.ServerMember;
import de.kaleidox.crystalshard.api.handling.listener.server.ban.ServerBanListener;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.handling.event.server.ban.ServerBanEventInternal;
import de.kaleidox.crystalshard.internal.items.server.interactive.BanInternal;
import de.kaleidox.crystalshard.internal.items.user.ServerMemberInternal;

public class GUILD_BAN_ADD extends HandlerBase {
    // Override Methods
    @Override
    public void handle(DiscordInternal discord, JsonNode data) {
        long serverId = data.get("guild_id")
                .asLong();
        Server server = discord.getServerCache()
                .getOrRequest(serverId, serverId);
        ServerMember user = ServerMemberInternal.getInstance(discord.getUserCache()
                .getOrCreate(discord, data.get("user")), server);
        Ban ban = new BanInternal(server, user);

        ServerBanEventInternal event = new ServerBanEventInternal(discord, ban);

        collectListeners(ServerBanListener.class, discord, server).forEach(listener -> discord.getThreadPool()
                .execute(() -> listener.onServerBanAdd(event)));
    }
}
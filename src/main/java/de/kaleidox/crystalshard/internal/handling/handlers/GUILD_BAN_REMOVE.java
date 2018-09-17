package de.kaleidox.crystalshard.internal.handling.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.handling.event.server.ban.ServerBanEventInternal;
import de.kaleidox.crystalshard.internal.items.server.ServerInternal;
import de.kaleidox.crystalshard.internal.items.server.interactive.BanInternal;
import de.kaleidox.crystalshard.internal.items.user.UserInternal;
import de.kaleidox.crystalshard.main.handling.listener.server.ban.ServerBanListener;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.items.server.interactive.Ban;
import de.kaleidox.crystalshard.main.items.user.ServerMember;

public class GUILD_BAN_REMOVE extends HandlerBase {
// Override Methods
    @Override
    public void handle(DiscordInternal discord, JsonNode data) {
        Server server = ServerInternal.getInstance(discord, data.get("guild_id").asLong());
        ServerMember user = UserInternal.getInstance(discord, data.get("user")).toServerMember(server);
        Ban ban = new BanInternal(server, user);
        
        ServerBanEventInternal event = new ServerBanEventInternal(discord, ban);
        
        collectListeners(ServerBanListener.class, discord, server).forEach(listener -> discord.getThreadPool()
                .execute(() -> listener.onServerBanAdd(event)));
    }
}

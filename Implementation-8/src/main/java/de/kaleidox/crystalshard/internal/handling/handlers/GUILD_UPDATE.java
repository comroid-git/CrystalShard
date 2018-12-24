package de.kaleidox.crystalshard.internal.handling.handlers;

import com.fasterxml.jackson.databind.JsonNode;

import de.kaleidox.crystalshard.api.entity.server.Server;
import de.kaleidox.crystalshard.api.handling.editevent.EditTrait;
import de.kaleidox.crystalshard.api.handling.listener.server.generic.ServerEditListener;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.handling.event.server.generic.ServerEditEventInternal;
import de.kaleidox.crystalshard.internal.items.server.ServerInternal;

import java.util.Set;

public class GUILD_UPDATE extends HandlerBase {
    // Override Methods
    @Override
    public void handle(DiscordInternal discord, JsonNode data) {
        Server server = discord.getServerCache()
                .getOrCreate(discord, data);

        Set<EditTrait<Server>> editTraits = ((ServerInternal) server).updateData(data);
        ServerEditEventInternal event = new ServerEditEventInternal(discord, server, editTraits);

        collectListeners(ServerEditListener.class, discord, server).forEach(listener -> discord.getThreadPool()
                .execute(() -> listener.onServerEdit(event)));
    }
}

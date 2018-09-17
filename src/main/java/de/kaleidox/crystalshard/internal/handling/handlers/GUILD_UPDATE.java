package de.kaleidox.crystalshard.internal.handling.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.handling.event.server.generic.ServerEditEventInternal;
import de.kaleidox.crystalshard.internal.items.server.ServerInternal;
import de.kaleidox.crystalshard.main.handling.editevent.EditTrait;
import de.kaleidox.crystalshard.main.handling.listener.server.generic.ServerEditListener;
import de.kaleidox.crystalshard.main.items.server.Server;

import java.util.Set;

public class GUILD_UPDATE extends HandlerBase {
// Override Methods
    @Override
    public void handle(DiscordInternal discord, JsonNode data) {
        Server server = ServerInternal.getInstance(discord, data); // get the old Server server
        
        Set<EditTrait<Server>> editTraits = ((ServerInternal) server).updateData(data);
        ServerEditEventInternal event = new ServerEditEventInternal(discord, server, editTraits);
        
        collectListeners(ServerEditListener.class, discord, server).forEach(listener -> discord.getThreadPool()
                .execute(() -> listener.onServerEdit(event)));
    }
}

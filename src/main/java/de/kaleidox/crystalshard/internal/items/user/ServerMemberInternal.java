package de.kaleidox.crystalshard.internal.items.user;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.items.server.ServerInternal;
import de.kaleidox.crystalshard.main.items.user.ServerMember;

public class ServerMemberInternal extends UserInternal implements ServerMember {
    public ServerMemberInternal(DiscordInternal discord,
                                ServerInternal serverInternal,
                                JsonNode data) {
        super(discord, data);
    }
}

package org.comroid.crystalshard.api.event.multipart.guild;

import org.comroid.crystalshard.api.entity.guild.Guild;
import org.comroid.crystalshard.api.event.multipart.APIEvent;

public interface GuildEvent extends APIEvent {
    Guild getGuild();
}

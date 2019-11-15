package de.comroid.crystalshard.api.event.multipart.guild;

import de.comroid.crystalshard.api.entity.guild.Guild;
import de.comroid.crystalshard.api.event.multipart.APIEvent;

public interface GuildEvent extends APIEvent {
    Guild getGuild();
}

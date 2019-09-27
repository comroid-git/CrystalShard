package de.kaleidox.crystalshard.api.event.guild;

import de.kaleidox.crystalshard.api.entity.guild.Guild;
import de.kaleidox.crystalshard.api.event.model.Event;

public interface GuildEvent extends Event {
    Guild getGuild();
}
